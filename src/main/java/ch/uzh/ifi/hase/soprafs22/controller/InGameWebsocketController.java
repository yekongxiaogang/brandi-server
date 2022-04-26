package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.entity.Player;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.ExampleMove;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.ExampleMoveGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.ExampleMovePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class InGameWebsocketController {

    @MessageMapping("/move")
    @SendTo("/client/move")
    public ExampleMoveGetDTO exampleMove(ExampleMovePostDTO exampleMovePostDTO, Principal principal) throws Exception {
        // get move from the client
        ExampleMove move = DTOMapper.INSTANCE.convertExampleMovePostDTOtoEntity(exampleMovePostDTO);

        // verify move validity and add Player details
        String username = principal.getName();
        move.setPlayer(new Player(username, 2, "randomUUID", "fake password", Color.BLUE));

        // notify subscribers with the move
        return DTOMapper.INSTANCE.convertEntityToExampleMoveGetDTO(move);
    }

    /**
     * We can use a method of this form to send a board state update to all the players that are currently connected
     * @param principal Authenticated user information
     * @return state of the current game
     */
    @MessageMapping("/connected")
    @SendTo("/client/connected")
    public String notifyConnected(Principal principal) throws Exception {
        System.out.println(principal.getName() + " is connecting...");
        return principal.getName() + " notifies subscribers that he's connected!";
    }

    @MessageMapping("/connected/{room}")
    @SendTo("/client/connected/{room})")
    public String greet(@DestinationVariable String room, Principal principal) throws Exception{
        System.out.println(principal.getName() + " joined room " + room);
        return principal.getName() + " joined room " + room;
    }
}
