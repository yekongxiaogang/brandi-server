package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.IdDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Game Controller
 * This class is responsible for handling all REST request that are related to
 * the game.
 * The controller will receive the request and delegate the execution to the
 * GameService and finally return the result.
 */

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    // Create game and add first user to it
    public String createGame(@RequestBody IdDTO lobbyLeaderId) {

        System.out.println("/game called");

        return gameService.createGame(lobbyLeaderId.getId());
    }

    @PutMapping("/game/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean joinGame(@PathVariable(name = "uuid") String uuid, Principal principal) {
        String username = principal.getName();
        Boolean success = gameService.joinGame(uuid, username);
        return success;
    }

    //For testing purposes, state should probably be taken from Websocket by clients
    @GetMapping("/game")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getGames() {
        List<Game> allGames = gameService.getGames();
        List<GameGetDTO> allGamesDTO = new ArrayList<>();
        for(Game game : allGames){
            allGamesDTO.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        
        return allGamesDTO;
    }

    @GetMapping("/game/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameByUuid(@PathVariable(name = "uuid") String uuid, Principal principal) {
        Game game = gameService.getGameByUuid(uuid, principal.getName());
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found by uuid");
        }
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }
}
