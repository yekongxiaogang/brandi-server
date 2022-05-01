package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;

public class HighlightMarblesDTO {

    private int index;
    private int[] marbles;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int[] getMarbles() {
        return marbles;
    }

    public void setMarbles(int[] marbles) {
        this.marbles = marbles;
    }
}
