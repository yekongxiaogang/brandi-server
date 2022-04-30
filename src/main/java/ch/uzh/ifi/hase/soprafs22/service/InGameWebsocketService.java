package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class InGameWebsocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public InGameWebsocketService(@Qualifier("gameRepository") GameRepository gameRepository,
    @Qualifier("userRepository") UserRepository userRepository,
    @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.lobbyRepository = lobbyRepository;
    }

    /* public Move verifyMove(Move move, String username){
        // Assign user to move, make move in Game, return move
        User user = userRepository.findByUsername(username);
        Optional<Long> optGameId = user.getCurrentGameId();
        optGameId.ifPresentOrElse(
            //If current game was found
            (gameId) ->{
                move.setUser(user);
                Optional<Game> optGame = gameRepository.findById(gameId);
                Game game;
                if(optGame.isPresent()){
                    game = optGame.get();
                    // Actually make the move and persist it
                    game.makeMove(move);
                    gameRepository.saveAndFlush(game);
                } else{
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong when executing your move");
                }
            },
            //If no current game found
            () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no ongoing game");
            }
        );
    } */

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
        move.setUser(user);
        // Actually make the move and persist it
        game.makeMove(move);
        gameRepository.saveAndFlush(game);
    
        // If everything went well, return the move that was made
        return move;
    }
}
