package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;

import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserIdDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;

import java.util.ArrayList;
import java.util.List;

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

    public LobbyGetDTO createLobby(@RequestBody UserIdDTO lobbyLeaderId) {

        Lobby createdLobby = lobbyService.createLobby(lobbyLeaderId.getLobbyLeaderId());
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);

    }

    // TODO: I think we go with PUT here as we update the Lobby's userlist? We have GET in spec
    // and we also need to know which user is joining and this goes into @requestbody
    @PutMapping("/lobby/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void joinLobby(@PathVariable(name = "uuid") String uuid, @RequestBody UserIdDTO userId ) {
        // join lobby
        lobbyService.joinLobby(uuid, userId.getLobbyLeaderId());
    }


    @GetMapping("/lobby/{lobbyUuid}/isFull")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean isFull(@PathVariable("lobbyUuid") String lobbyUuid){
        //TODO: implement
        return lobbyService.isFull(lobbyUuid);
    }

    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();
        for (Lobby lobby: lobbies){
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOs;
    }

   
}
