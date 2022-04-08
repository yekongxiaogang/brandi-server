package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to
 * the game
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
    @Qualifier("userRepository") UserRepository userRepository,
    @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {

        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.lobbyRepository = lobbyRepository;
    }

    public Game getGame(Long Id) {
        Optional<Game> game = this.gameRepository.findById(Id);
        if(game.isPresent()){
            return game.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find Game");
        }
    }

    //TODO: I guess if we have lobbyLeader then we need one user assigned to lobby in the Lobby class?
    // Also a lobbyLeader is the one creating the lobby but when he leaves it we randomly assign next one?
    // Then in such implementation I guess we use User instead of Long here as we can easily check if lobbyLeader is in lobby through players ArrayList?
    public Game createGame(Long lobbyLeaderId) {
        Optional<User> lobbyLeader = this.userRepository.findById(lobbyLeaderId);
        if(lobbyLeader.isPresent()){
            Game newGame = new Game(lobbyLeader.get());
            newGame = gameRepository.save(newGame);
            gameRepository.flush();
            System.out.println("Created Information for Game: " + newGame.getId() + newGame.getPlayerStates());
            return newGame;
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find Lobbyleader");
        }
        
    }
    
}