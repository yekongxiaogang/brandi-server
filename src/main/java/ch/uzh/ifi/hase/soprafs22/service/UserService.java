package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + id + " does not exist.");
        }

        return user.get();
    }

    public User getUser(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);

        checkIfUserExists(newUser);
        newUser.setCreatedDate(Instant.now());
        newUser.setPassword((new BCryptPasswordEncoder()).encode(newUser.getPassword()));

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(Long id, UserUpdateDTO userUpdateDTO, String updatingUsername) {
        User user = this.userRepository.findById(id).orElse(null);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + id + " does not exist.");
        } else if(user.getUsername() != updatingUsername) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to change the birthday of other users than yourself.");
        }

        userRepository.save(user);
        userRepository.flush();

        return user;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByName = userRepository.findByUsername(userToBeCreated.getPassword());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null && userByName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "username and the name", "are"));
        }
        else if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
        }
    }

    /**
     * this is a helper method that will check if a user with the same username is
     * in the database
     *
     * @see User
     */
    public User authenticateUser(User user) {
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        return userFromDatabase;
    }

    public List<String> getGameUuidsOfUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        List<String> gameUuids = new ArrayList<>();

        // If user exists: Add uuids to list, else throw error
        optUser.ifPresentOrElse(
            (user) -> {
                List<Game> games = user.getGames();
                games.forEach((game) -> gameUuids.add(game.getUuid()));
            }, 
            () -> {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found");
            }
        );
        
        return gameUuids;
    }
}
