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

}
