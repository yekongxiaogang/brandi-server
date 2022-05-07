package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import ch.uzh.ifi.hase.soprafs22.entity.BoardState;
import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.PlayerState;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MoveGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MovePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.SelectMarbleResponseDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameLogicService;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.InGameWebsocketService;

import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Set;


@Controller
public class InGameWebsocketController {

    private final InGameWebsocketService inGameWebsocketService;
    private final GameLogicService gameLogicService;
    private final GameService gameService;
    private UserService userService;

    InGameWebsocketController(InGameWebsocketService service, GameService gameService, GameLogicService gameLogicService, UserService userService) {
        this.inGameWebsocketService = service;
        this.gameLogicService = gameLogicService;
        this.gameService = gameService;
        this.userService = userService;
    }


    @MessageMapping("/websocket/{uuid}/move")
    public void move(@DestinationVariable String uuid, MovePostDTO MovePostDTO, Principal principal) throws Exception {
        // Get move, username, game
        Move move = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(MovePostDTO);
        String username = principal.getName();
        Game game = gameService.getGameByUuid(uuid, username);

        // verify move validity, make move in game, add Player details to move for returning
        move = inGameWebsocketService.verifyMove(game, move, username);

        // move == null means it wasnt users turn or no cards left, simply ignore 
        if(move == null) return;

        inGameWebsocketService.notifyPlayersAfterMove(move, uuid);
    }

    @MessageMapping("/websocket/{uuid}/join")
    public void joinGameByUuid(@DestinationVariable String uuid, Principal principal) throws Exception {
        System.out.println(principal.getName() + " just joined a game");
        Game game = gameService.getGameByUuid(uuid, principal.getName());
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game to join not found by uuid");
        }

        PlayerState playerState = gameService.playerJoined(game, principal.getName());

        // if(playerState == null) return;

        // provide the new user with the current Game State
        inGameWebsocketService.notifySpecificUser("/client/state", principal.getName(), DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));

        // provide the new user with the his hand
        inGameWebsocketService.notifySpecificUser("/client/cards", principal.getName(), playerState.getPlayerHand());

        // provide the user's updated information to all other members in the lobby
        UserGetDTO user = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.getUser(principal.getName()));
        inGameWebsocketService.notifyAllOtherGameMembers("/client/player/joined", game, principal.getName(), playerState);
    }


    @MessageMapping("/websocket/{uuid}/leave")
    public void leaveGameByUuid(@DestinationVariable String uuid, Principal principal) throws Exception {
        System.out.println(principal.getName() + " just joined a game");
        Game game = gameService.getGameByUuid(uuid, principal.getName());
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found by uuid");
        }

        PlayerState playerState = gameService.playerLeft(game, principal.getName());

        // provide the user's updated information to all other members in the lobby
        UserGetDTO user = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.getUser(principal.getName()));
        inGameWebsocketService.notifyAllOtherGameMembers("/client/player/left", game, principal.getName(), playerState);
    }

    @MessageMapping("/websocket/{uuid}/select/card")
    public void selectCard(@DestinationVariable String uuid, CardDTO card, Principal principal) throws Exception {
        System.out.println(principal.getName() + " selected a card");

        Game game = gameService.getGameByUuid(uuid, principal.getName());
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found by uuid");
        }
        
        String username = principal.getName();
        Set<Ball> balls = game.getBoardstate().getBalls();
        Set<Integer> marblesSet = gameLogicService.highlightBalls(card.getRank(), balls, game.getPlayerState(username).getColor());

        Boolean movePossible = inGameWebsocketService.selectCard(game, card, username, marblesSet);
        if(movePossible){
            return;
        }
        // If user can choose other card and play with that, return nothing. 
        // If no playable card, delete cards and move to next player
        PlayerState playerState = game.getPlayerState(username);
        for(Card cardInHand: playerState.getPlayerHand().getActiveCards()){
            Set<Integer> possibleMarbles = gameLogicService.highlightBalls(cardInHand.getRank(), balls, playerState.getColor());
            //TODO: Could send list of playable cards to user here
            if(!possibleMarbles.isEmpty()){
                // User has other card to make a move, ignore 
                return;
            }
        }
        inGameWebsocketService.noMovePossible(game, username);
    }


    @MessageMapping("/websocket/{uuid}/select/marble")
    public void selectMarble(@DestinationVariable String uuid, SelectMarbleDTO selectMarbleDTO, Principal principal) throws Exception {
        System.out.println(principal.getName() + " selected a marble");

        Game game = gameService.getGameByUuid(uuid, principal.getName());
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found by uuid");
        }

        PlayerState playerState = game.getPlayerState(principal.getName());

        // highlight possible moves

        BoardState boardState = game.getBoardstate();

        Set<Ball> balls = boardState.getBalls();

        Long ballId = Long.valueOf(selectMarbleDTO.getMarbleId());

        Ball ball = boardState.getBallById(ballId);

        Set<Integer> possibleMoves = gameLogicService.getPossibleMoves(selectMarbleDTO.getRank(), balls, ball);

        Set<Integer> highlightedHolesSet = gameLogicService.getPossibleDestinations(possibleMoves, ball);

        int[] highlightedHoles = highlightedHolesSet.stream().mapToInt(Integer::intValue).toArray();

        SelectMarbleResponseDTO selectMarbleResponseDTO = new SelectMarbleResponseDTO();
        selectMarbleResponseDTO.setMarbleId(selectMarbleDTO.getMarbleId());
        selectMarbleResponseDTO.setHighlightHoles(highlightedHoles);

        // provide the user with a list of marbles he could move
        inGameWebsocketService.notifySpecificUser("/client/highlight/holes", principal.getName(), selectMarbleResponseDTO);
    }


    /**
     * We can use a method of this form to send a board state update to all the players that are currently connected
     * to the same game.
     *
     * @param principal Authenticated user information
     */
    @MessageMapping("/websocket/{uuid}/test")
    public void test(@DestinationVariable String uuid, Principal principal) throws Exception {

        // we still have to verify if the player is actually playing in the game with that uuid

        Game game = gameService.getGameByUuid(uuid, principal.getName());

        // the payload can be a anything you want to send to the clients
        inGameWebsocketService.notifyAllGameMembers("/client/test", game, principal.getName() + " sent a test message!");
    }

}
