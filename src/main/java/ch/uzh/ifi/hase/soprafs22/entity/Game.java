package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

public class Game {
    private Boolean gameOver;
    private Boolean gameOn;
    private Integer roundsPlayed;
    private ArrayList<PlayerState> playerStates;
    private BoardState boardstate;


    public Game(Boolean gameOver, Boolean gameOn, ArrayList<PlayerState> playerStates, BoardState boardstate) {
        this.gameOver = gameOver;
        this.gameOn = gameOn;
        this.roundsPlayed = 0;
        this.playerStates = playerStates;
        this.boardstate = boardstate;
    }

    public void startGame(){
        return;
    }

    public void startNewRound(){
        return;
    }

    public void setGameOn(Boolean gameOn) {
        this.gameOn = gameOn;
    }

    public Boolean checkGameOver(){
        return this.gameOver;
    }

    public Boolean checkPlayersOnline(){
        // TODO: Implement checkPlayersOnline
        return false;
    }

    public void pauseGame(){
        this.gameOn = false;
    }

    public void resumeGame(){
        this.gameOn = true;
    }

    public void endGame(){
        this.gameOver = true;
    }
}
