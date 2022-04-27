package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MoveGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MovePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.InGameWebsocketService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class InGameWebsocketController {


    private final InGameWebsocketService inGameWebsocketService;
    private final GameService gameService;

    InGameWebsocketController(InGameWebsocketService service, GameService gameService) {
        this.inGameWebsocketService = service;
        this.gameService = gameService;
    }


    // TODO: need to add {uuid} and notify all other players (refer to test function below)
    @MessageMapping("/websocket/move")
    @SendTo("/client/move")
    public MoveGetDTO move(MovePostDTO MovePostDTO, Principal principal) throws Exception {
        // get move from the client
        Move move = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(MovePostDTO);

        // verify move validity and add Player details
        String username = principal.getName();
        move = inGameWebsocketService.verifyMove(move, username);

        // notify subscribers with the move
        /* TODO: Should this return the whole gameState instead of only a move? 
        Otherwise the client needs to get GameState manually */
        return DTOMapper.INSTANCE.convertEntityToMoveGetDTO(move);
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

        Game game = gameService.getGameByUuid(uuid);

        // the payload can be a anything you want to send to the clients
        inGameWebsocketService.notifyAllGameMembers("/client/test", game, principal.getName() + " sent a test message!");
    }

}
