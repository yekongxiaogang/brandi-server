package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.entity.websocket.Move;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MoveGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.websocket.MovePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.InGameWebsocketService;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class InGameWebsocketController {

    private final InGameWebsocketService service;

    InGameWebsocketController(InGameWebsocketService service) {
        this.service = service;
    }
    
    @MessageMapping("/move")
    @SendTo("/client/move")
    public MoveGetDTO move(MovePostDTO MovePostDTO, Principal principal) throws Exception {
        // get move from the client
        Move move = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(MovePostDTO);

        // verify move validity and add Player details
        String username = principal.getName();
        move = service.verifyMove(move, username);

        // notify subscribers with the move
        /* TODO: Should this return the whole gameState instead of only a move? 
        Otherwise the client needs to get GameState manually */
        return DTOMapper.INSTANCE.convertEntityToMoveGetDTO(move);
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
