package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserIdDTO;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;
import javassist.expr.Instanceof;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Lobby Controller
 * This class is responsible for handling all REST request that are related to
 * the lobby.
 * The controller will receive the request and delegate the execution to the
 * LobbyService and finally return the result.
 */

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Lobby createLobby(@RequestBody UserIdDTO lobbyLeaderId) {

        Long leaderId = lobbyLeaderId.getLobbyLeaderId();
        System.out.println(leaderId.getClass().getName());
        // create lobby
        //TODO: Implement LobbyGetDTO to return only the wanted info about lobby and users in lobby
        return lobbyService.createLobby(leaderId);
    }

    // TODO: I think we go with PUT here as we update the Lobby's userlist? We have GET in spec
    // and we also need to know which user is joining and this goes into @requestbody
    @PutMapping("/lobby/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable("lobbyUuid") String lobbyUuid,
    @RequestBody Long userId ) {
        // join lobby
        lobbyService.joinLobby(lobbyUuid, userId);
    }

    @GetMapping("/lobby/{lobbyUuid}/isFull")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean isFull(@RequestBody String lobbyUuid){
        //TODO: implement
        return false;
    }
   
}
