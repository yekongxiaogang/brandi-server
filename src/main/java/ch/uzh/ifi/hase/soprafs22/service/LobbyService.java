package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
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

/**
 * Lobby Service
 * This class is the "worker" and responsible for all functionality related to
 * the lobby
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

    public Lobby createLobby(Long leaderId) {
        try {
            Optional<User> user = this.userRepository.findById(leaderId);
            if(!user.isPresent()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldnt find user who created lobby");
            }
            // Create new lobby
            Lobby newLobby = new Lobby(user.get());
                
            // Save it in the repo
            lobbyRepository.save(newLobby);
            lobbyRepository.flush();

            log.debug("Created Information for Lobby: {}", newLobby);
            return newLobby;
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong when creating your lobby");
        }
    }

    public void joinLobby(String lobbyUuid, Long userId) {
        // Search for Lobby and User; if found add User to Lobby; otherwise throw exception
        try {
            Optional<Lobby> optionalLobby = lobbyRepository.findByLobbyUuid(lobbyUuid);
            Lobby lobbyByUuid = optionalLobby.get();

            Optional<User> optionalUser = userRepository.findById(userId);
            User joiningUser = optionalUser.get();

            lobbyByUuid.addPlayer(joiningUser);
            lobbyRepository.saveAndFlush(lobbyByUuid);
          } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
          }
    }

    public Boolean isFull(String lobbyUuid) {
        try {
            Optional<Lobby> optionalLobby = lobbyRepository.findByLobbyUuid(lobbyUuid);
            if(!optionalLobby.isPresent()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
            }
            Lobby lobby = optionalLobby.get();
            return lobby.isFull();
          } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong while getting your lobby");
          }
    }

    public List<Lobby> getLobbies(){
        return this.lobbyRepository.findAll();
    }
    
}