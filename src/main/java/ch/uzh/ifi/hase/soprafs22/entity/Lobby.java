package ch.uzh.ifi.hase.soprafs22.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

@Entity
public class Lobby {

    @Id
    @GeneratedValue
    private Long lobbyId;


    private String lobbyUuid;
    private Boolean isInGame;
    private Integer playerLimit;

    @OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
    private List<User> users;

    // https://www.yawintutor.com/unexpected-error-no-default-constructor-for-entity/
    public Lobby() {
    }


    //TODO EDIT: Now we create UUID for lobby when constructing it and
    // set isInGame to false
    public Lobby(User lobbyLeader) {
        this.users = new ArrayList<>();
        this.users.add(lobbyLeader);
        this.lobbyUuid = UUID.randomUUID().toString();
        this.isInGame = false;
        this.playerLimit = 4;
    }

    public void startGame(){
        this.isInGame = true;
        //TODO: Start game, persist in DB
    }

    public String getInviteLink(){
        // TODO: Should this return only id or a full Link? full link would need to distinguish between prod and dev
        return this.lobbyUuid;
    }

    public void addPlayer(User player){
        if(!this.isFull()){
            users.add(player);
        } else{
            // TODO: Should this throw error?
            // I guess but in spec we got "not authorized to join this lobby" and
            // I dunno whether it resorts to this or some other authorization problem
            // like are we making password guarded lobbys also? If we do then that's later anyways
            System.out.println("Lobby already has 4 players, cant add new player");
        }
    }

    public Boolean isFull(){
        return this.users.size() >= this.playerLimit;
    }

    public Long getLobbyId() {
        return this.lobbyId;
    }

    // FIXME: Same as getInviteLink()
    public String getLobbyUuid() {
        return this.lobbyUuid;
    }    

    public List<User> getUsers(){
        return this.users;
    }

    public Boolean getIsInGame(){
        return this.isInGame;
    }

}
