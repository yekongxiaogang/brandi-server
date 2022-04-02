package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

public class Lobby {
    private Integer lobbyId;
    private String lobbyUuid;
    private Boolean isInGame;
    private Integer playerLimit;
    private ArrayList<Player> players;


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

    public void addPlayer(Player player){
        if(players.size() < 4){
            players.add(player);
        } else{
            // TODO: Should this throw error?
            System.out.println("Lobby already has 4 players, cant add new player");
        }
    }

    public Integer getLobbyId() {
        return this.lobbyId;
    }

    // FIXME: Same as getInviteLink()
    public String getLobbyUuid() {
        return this.lobbyUuid;
    }

    
}
