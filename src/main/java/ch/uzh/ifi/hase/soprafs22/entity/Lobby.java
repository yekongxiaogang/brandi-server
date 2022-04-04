package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

import javax.persistence.Id;

public class Lobby {

    @Id
    @GeneratedValue
    private String lobbyId;

    private String lobbyUuid;
    private Boolean isInGame;
    private Integer playerLimit;
    private ArrayList<User> players;
    private User lobbyLeader;

    //TODO EDIT: Now we create UUID for lobby when constructing it and
    // set isInGame to false
    public Lobby(User lobbyLeader) {
        this.lobbyUuid = UUID.randomUUID().toString();
        this.isInGame = false;
        this.playerLimit = 4;
        this.lobbyLeader = lobbyLeader;
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
        if(this.isFull()){
            players.add(player);
        } else{
            // TODO: Should this throw error?
            // I guess
            System.out.println("Lobby already has 4 players, cant add new player");
        }
    }

    public Boolean isFull(){
        return this.players.size() < 4;
    }

    public Integer getLobbyId() {
        return this.lobbyId;
    }

    // FIXME: Same as getInviteLink()
    public String getLobbyUuid() {
        return this.lobbyUuid;
    }

    public User getLobbyLeader() {
        return this.lobbyLeader;
    }

    public void setLobbyLeader(User lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
    }

    
}
