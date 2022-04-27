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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public String createGame(Long userId) {
        Optional<User> optUser = this.userRepository.findById(userId);
        if(optUser.isPresent()){
            // Create Game and set passed user as player in that game, return game
            User user = optUser.get();
            Game newGame = new Game(user);
            newGame = gameRepository.saveAndFlush(newGame);
            
            // Add game to list of games in user, persist in DB
            user.addGame(newGame);
            userRepository.saveAndFlush(user);
            System.out.println("Created Information for Game: " + newGame.getId());
            System.out.println(newGame.getUuid());
            return newGame.getUuid();
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find Lobbyleader");
        }
        
    }

    public Boolean joinGame(String uuid, String username){
        Optional<Game> optGame = gameRepository.findByUuid(uuid);
        if(optGame.isPresent()){
            // If game exists, add User to game and persist in DB
            Game game = optGame.get();
            User user = userRepository.findByUsername(username);
            Boolean added = game.addPlayer(user);
            if(!added) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't add Player"); }
            game = gameRepository.saveAndFlush(game);
            System.out.println(String.format("Added %s to game %d", username, game.getId()));

            // Add game to list of games in user, persist in DB
            user.addGame(game);
            userRepository.saveAndFlush(user);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find Game");
        }
    }
    

    public List<Game> getGames() {
        return gameRepository.findAll();
    }
    
}