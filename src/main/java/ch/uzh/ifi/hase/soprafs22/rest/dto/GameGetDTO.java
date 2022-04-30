package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs22.entity.BoardState;

import ch.uzh.ifi.hase.soprafs22.entity.PlayerState;

public class GameGetDTO {
    private String uuid;
    private Long id;
    private Boolean gameOver;
    private Boolean gameOn;
    private Integer roundsPlayed;
    private ArrayList<PlayerState> playerStates;
    private BoardState boardstate;


    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isGameOver() {
        return this.gameOver;
    }

    public Boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Boolean isGameOn() {
        return this.gameOn;
    }

    public Boolean getGameOn() {
        return this.gameOn;
    }

    public void setGameOn(Boolean gameOn) {
        this.gameOn = gameOn;
    }

    public Integer getRoundsPlayed() {
        return this.roundsPlayed;
    }

    public void setRoundsPlayed(Integer roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public ArrayList<PlayerState> getPlayerStates() {
        return this.playerStates;
    }

    public void setPlayerStates(ArrayList<PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    public BoardState getBoardstate() {
        return this.boardstate;
    }

    public void setBoardstate(BoardState boardstate) {
        this.boardstate = boardstate;
    }

}
