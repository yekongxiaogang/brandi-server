package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

public class PlayerState {
    private Player player;
    private Boolean isPlaying;
    private Integer team;
    private Boolean playerStatus;
    private PlayerHand playerHand;

    public PlayerState(Player player, Integer team, Boolean playerStatus, PlayerHand playerHand) {
        this.player = player;
        this.isPlaying = true;
        this.team = team;
        this.playerStatus = playerStatus;
        this.playerHand = playerHand;
    }

    public Boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Integer getTeam() {
        return this.team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Boolean getPlayerStatus() {
        return this.playerStatus;
    }

    public void setPlayerStatus(Boolean playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerHand getPlayerHand() {
        return this.playerHand;
    }

    public void drawCards(ArrayList<Card> cards){
        this.playerHand.drawCards(cards);
    }


    /* public void setPlayerHand(PlayerHand playerHand) {
        this.playerHand = playerHand;
    } */

}
