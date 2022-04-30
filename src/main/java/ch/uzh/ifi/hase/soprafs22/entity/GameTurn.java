package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import ch.uzh.ifi.hase.soprafs22.constant.Rank;

public class GameTurn {

    // When making a move player:
    // 1. Chooses card
    // 2. Can switch to another card
    // 3. Chooses ball with which to move => possible moves are highlighted
    // 4. Can switch to another ball
    // 5. Clicks on one of the highlighted positions to go there

    private final PlayerState activePlayer;

    private BoardState boardState;
    private PlayerHand playerHand;

    public GameTurn(PlayerState activePlayer, BoardState boardState) {
        this.activePlayer = activePlayer;
        this.playerHand = activePlayer.getPlayerHand();
    }

    public void playCard(Card card, PlayerState activePlayer, Ball ball) {
        // TODO: will need to remove card from player's hand and
        // make move with makeMove
        // have to remove exact card; need to take care of possible multiplicities in hand
        // will need defined destination
        // int chosenMove = someInt;
        makeMove(ball);
        // activePlayer.getHand().deleteCard(card);
        playerHand.deleteCard(card);

        // TODO: WEBSOCKET CALL TO DISPLAY THE MOVE AND DELETE CARD
    }

    // TODO: this should get destination from client and then update ballPos accordingly
    public void makeMove(Ball ball) {
        int startPos = ball.getPosition();
        //ball.setPosition(startPos + chosenMove);
    }

    // FOR SINGLE CARD
//    public void calculateMove(Card card) {
//
//        Rank cardRank = card.getRank();
//        if (cardRank != Rank.JOKER) {
//            updatePossibleMoves(cardRank);
//        }
//        else {
//            Rank jokerRank = chooseJokerRank();
//            updatePossibleMoves(jokerRank);
//        }
//    }

    public void endTurn() {
        // increment roundsPlayed
        // WEBSOCKET call to display update to every player
        // persist changes in repository
    }

}
