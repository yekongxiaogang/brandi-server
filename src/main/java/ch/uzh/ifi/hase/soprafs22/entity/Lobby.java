package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

import javax.persistence.Id;

public class Lobby {
    @Id
    private String lobbyUuid;

    private Integer lobbyId;
    private Boolean isInGame;
    private Integer playerLimit;
    private ArrayList<User> players;


    public Lobby(Integer lobbyId, String lobbyUuid, Boolean isInGame) {
        this.lobbyId = lobbyId;
        this.lobbyUuid = lobbyUuid;
        this.isInGame = isInGame;
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
        if(this.isFull()){
            players.add(player);
        } else{
            // TODO: Should this throw error?
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

    
}
