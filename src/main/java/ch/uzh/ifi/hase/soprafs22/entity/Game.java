package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs22.constant.BallState;
import ch.uzh.ifi.hase.soprafs22.constant.Color;

public class Game {
    private Boolean gameOver;
    private Boolean gameOn;
    private Integer roundsPlayed;
    private ArrayList<PlayerState> playerStates;
    private BoardState boardstate;
    private Deck deck;


    public Game(ArrayList<Player> players) {
        this.gameOver = false;
        this.gameOn = true;
        this.roundsPlayed = 0;
        this.deck = new Deck();
        this.initPlayerStates(players);
        this.initBoardState();
        this.startNewRound();
    }

    /* Create balls for each player, store in boardstate */
    private void initBoardState(){
        ArrayList<Ball> balls = new ArrayList<>();
        Integer count = 0;
        // Add 4 balls for each playerColor
        for(Color color: Color.values()){
            for(int i = 0; i < 4; i++){
                balls.add(new Ball(color, count + i, -1, BallState.BASE));
            }
            count += 4;
        }
        this.boardstate = new BoardState(balls);
    }

    /* Create PlayerState for every player with 6 cards in playerHand */
    private void initPlayerStates(ArrayList<Player> players){
        for(Player player : players){
            PlayerHand playerHand = new PlayerHand();
            ArrayList<Card> cards = new ArrayList<>();

            for(int i = 0; i < 6; i++){
                cards.add(this.deck.drawCard());
            }

            playerHand.drawCards(cards);
            this.playerStates.add(new PlayerState(player, 0, true, playerHand));
        }
    }

    public void startGame(){
        return;
    }

    public void startNewRound(){
        this.roundsPlayed += 1;

        List<Integer> amounts = Arrays.asList(6, 5, 4, 3, 2);
        Integer numCardsToPlay = roundsPlayed % 5;

        // Draw new cards for each player
        for(PlayerState playerState : this.playerStates){
            ArrayList<Card> cards = new ArrayList<>();

            for(int i = 0; i < amounts.get(numCardsToPlay); i++){
                cards.add(this.deck.drawCard());
            }

            playerState.drawCards(cards);
        }
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
