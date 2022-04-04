package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private final LobbyRepository lobbyRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
    @Qualifier("userRepository") UserRepository userRepository,
    @Qualifier("gameRepository") GameRepository gameRepository) {

        this.lobbyRepository = lobbyRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Lobby getLobby(Long Id) {
        Optional<Lobby> lobby = this.lobbyRepository.findById(Id);
        if(lobby.isPresent()){
            return lobby.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find Lobby");
        }
    }

    public Lobby createLobby(User lobbyLeader) {
        // Create new lobby
        Lobby newLobby = new Lobby(lobbyLeader);
        
        // Save it in the repo
        lobbyRepository.save(newLobby);
        lobbyRepository.saveAndFlush();

        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    private void joinLobby(String lobbyUuid, User joiningUser) {
        // TODO: Do we search for joiningUser in the repo just in case?
        // Kinda seems pointless as that's the one making the request
        try {
            Optional<Lobby> optionalLobby = lobbyRepository.findByUuid(lobbyUuid);
            Lobby lobbyByUuid = optionalLobby.get();
            lobbyByUuid.addPlayer(joiningUser);
          } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
          }
    }
    
}