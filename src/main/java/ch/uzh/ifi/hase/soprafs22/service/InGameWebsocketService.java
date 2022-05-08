package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.PlayerState;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.CardDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.HighlightMarblesDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MoveGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class InGameWebsocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Autowired
    public InGameWebsocketService(@Qualifier("gameRepository") GameRepository gameRepository,
    @Qualifier("userRepository") UserRepository userRepository) {

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public void notifyAllGameMembers(String route, Game game, /*Principal principal,*/ Object payload) {
        List<String> sentTo = new ArrayList<>();
        game.getPlayerStates().forEach((playerState) -> {
            sentTo.add(playerState.getPlayer().getUsername());
        });
//        System.out.println("created sendTo list");
//        System.out.println(sentTo);

        sentTo.forEach((send) -> {
//            String msg = send + " is getting a notification from " + principal.getName();
//            System.out.println(msg);
            simpMessagingTemplate.convertAndSendToUser(send, route, payload);
        });
    }

    /**
     * Send a notification to all game members except 'userName'
     * @param route
     * @param game
     * @param userName
     * @param payload
     */
    public void notifyAllOtherGameMembers(String route, Game game, String userName, Object payload) {
        List<String> sentTo = new ArrayList<>();
        game.getPlayerStates().forEach((playerState) -> {
            sentTo.add(playerState.getPlayer().getUsername());
        });

        sentTo.forEach((send) -> {
            if(!send.equals(userName)) {
                simpMessagingTemplate.convertAndSendToUser(send, route, payload);
            }
        });
    }

    public void notifySpecificUser(String route, String userName, Object payload) {
        simpMessagingTemplate.convertAndSendToUser(userName, route, payload);
    }


   /* Assign user to move, make move in Game, return move */
    public Move verifyMove(Game game, Move move, String username){
        // Add user details to move so that everybody knows who made the move
        User user = userRepository.findByUsername(username);
        if(this.checkHasNoCardsLeft(game, username)){
            System.out.println(username + " has no cards left to make a move");
            return null;
        }

        // User is not nextUser to play
        String nextPlayer = game.getNextTurn().getPlayer().getUsername();
        if(!nextPlayer.equals(username)){
            return null;
        }

        move.setUser(user);
        // Actually make the move and persist it
        game.makeMove(move);

        gameRepository.saveAndFlush(game);
    
        // If everything went well, return the move that was made
        return move;
    }
    
    public void notifyPlayersAfterMove(Move move, String uuid) {
        // Need to fetch game here, not in controller because no proxy error otherwise
        Optional<Game> optGame = gameRepository.findByUuid(uuid);
        if(optGame.isEmpty()) return;
        Game game = optGame.get();

        MoveGetDTO moveDTO = DTOMapper.INSTANCE.convertEntityToMoveGetDTO(move);
        String username = move.getUser().getUsername();

        this.notifyAllGameMembers("/client/move", game, moveDTO); 

        

        PlayerState nextUser = game.getNextTurn();
        if(nextUser == null){ // No user can play any cards anymore -> Start new round
            game.startNewRound();;
            nextUser = game.getNextTurn();
            for(PlayerState playerState: game.getPlayerStates()){
                // Send new Cards to all users
                this.notifySpecificUser("/client/cards", playerState.getPlayer().getUsername(), playerState.getPlayerHand());
                // Send next user to all users
                this.notifyAllGameMembers("/client/nextPlayer", game, nextUser.getPlayer());
            }
        } else{
            // Send next user to all users, send updated cards to user that moved
            this.notifyAllGameMembers("/client/nextPlayer", game, nextUser.getPlayer());
            PlayerState state = game.getPlayerState(username);
            this.notifySpecificUser("/client/cards", username, state.getPlayerHand());
        }

        gameRepository.saveAndFlush(game);
    }

    public Boolean checkIsNext(Game game, String username){
        // User is not nextUser to play
        String nextPlayer = game.getNextTurn().getPlayer().getUsername();
        if(nextPlayer.equals(username)){
            return true;
        }
        return false;
    }

    /* public void checkForSurrender(String username, Game game){
        PlayerState state = game.getPlayerState(username);
        Set<Ball> balls = game.getBoardstate().getBalls();
        // If user can choose other card and play with that, return nothing. If no playable card, delete cards and move to next player
        for(Card cardInHand: state.getPlayerHand().getActiveCards()){
            Set<Integer> possibleMarbles = gameLogicService.highlightBalls(cardInHand.getRank(), balls, state.getColor());
            if(!possibleMarbles.isEmpty()){
                return;
            }
        }
        // delete cards and move to next player
        game.surrenderCards(username);
        gameRepository.saveAndFlush(game);
    } */

    public Boolean checkHasNoCardsLeft(Game game, String username){
        Set<Card> cards = game.getPlayerState(username).getPlayerHand().getActiveCards();
        return cards.isEmpty();
    }

    public Boolean selectCard(Game game, CardDTO card, String username, Set<Integer> marblesSet){
        PlayerState playerState = game.getPlayerState(username);

        // choose marbles adequately to chosen card
        Set<Ball> balls = game.getBoardstate().getBalls();

        // check if his turn
        Boolean isNext = this.checkIsNext(game, username);
        if(!isNext) return true;
        
        if(marblesSet.isEmpty()){
            return false;
        }
        // check marblesset empty
        //if so, then check all cards
        // notify next
        int[] marbles = marblesSet.stream().mapToInt(Integer::intValue).toArray();

        HighlightMarblesDTO highlightMarblesDTO = new HighlightMarblesDTO();
        highlightMarblesDTO.setIndex(card.getIndex());
        highlightMarblesDTO.setMarbles(marbles);

        // provide the user with a list of marbles he could move
        this.notifySpecificUser("/client/highlight/marbles", username, highlightMarblesDTO);
        return true;
    }

    /**
     * surrender cards, move to next user
     * @param game
     * @param username
     */
    public void noMovePossible(Game game, String username){
        // Need to refetch game here because no proxy error otherwise
        Optional<Game> optGame = gameRepository.findByUuid(game.getUuid());
        if(optGame.isEmpty()) return;
        game = optGame.get();

        // User can choose no other card and play with that: Delete cards
        game = this.surrenderCards(game, username);
        // Move to next user
        PlayerState nextUser = game.getNextTurn();
        if(nextUser == null){
            // Send new Cards to all users
            game.startNewRound();
            nextUser = game.getNextTurn();
            for(PlayerState state: game.getPlayerStates()){
                this.notifySpecificUser("/client/cards", state.getPlayer().getUsername(), state.getPlayerHand());
                this.notifyAllGameMembers("/client/nextPlayer", game, nextUser.getPlayer());
            }
        } else{
            // Send next user to all users
            this.notifyAllGameMembers("/client/nextPlayer", game, nextUser.getPlayer());
            PlayerState state = game.getPlayerState(username);
            this.notifySpecificUser("/client/cards", username, state.getPlayerHand());
        }
    }

    private Game surrenderCards(Game game, String username) {  
        game.surrenderCards(username);
        gameRepository.saveAndFlush(game);
        return game;
    }
}
