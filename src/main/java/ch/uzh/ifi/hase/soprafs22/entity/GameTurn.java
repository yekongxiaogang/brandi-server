package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.entity.Player;

public class GameTurn {

    private Player activePlayer;

    public GameTurn(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void playCard(Card card) {
        // TODO: will need to remove card from player's hand and
        // make move with makeMove
    }

    public int calculateMoves() {
        // TODO: return possible moves combination
        // not sure about return type here
    }

    public void makeMove(Rank cardRank) {
        // TODO: move with a given card
    }

    public void endTurn() {
        // TODO: do we keep track of played turns, their number?
        // do we just create new GameTurn every time a player plays his turn?
        // or do we enable 1 action per turn and then just change the vars of GameTurn?
    }
    
}
