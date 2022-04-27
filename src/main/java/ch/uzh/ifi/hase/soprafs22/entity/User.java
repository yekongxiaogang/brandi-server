package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdDate;

    @ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "game_id", referencedColumnName = "id")
    private List<Game> games;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.games = new ArrayList<Game>() ;
    }

    public User() {
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public void removeGame(Game game){
        this.games.remove(game) ;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public Optional<Game> getGameById(Long id){
        for(Game game : this.games){
            if(game.getId() == id){return Optional.of(game);}
        }
        return Optional.empty();
    }


    /*
     * Returns game in list of games that is currently active, if more than one is active(shouldnt be possible), just returns first one
     * TODO: Make sure User is only in one active game at a time
    */
    public Optional<Long> getCurrentGameId(){
        for(Game game : this.games){
            if(game.getGameOn()){
                return Optional.of(game.getId());
            }
        }
        return Optional.empty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String name) {
        this.password = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    
}
