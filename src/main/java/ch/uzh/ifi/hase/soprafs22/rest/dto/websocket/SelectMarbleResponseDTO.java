package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;

public class SelectMarbleResponseDTO {

    private int marbleId;
    private int[] highlightHoles;

    public int getMarbleId() {
        return marbleId;
    }

    public void setMarbleId(int marbleId) {
        this.marbleId = marbleId;
    }

    public int[] getHighlightHoles() {
        return highlightHoles;
    }

    public void setHighlightHoles(int[] highlightHoles) {
        this.highlightHoles = highlightHoles;
    }
}
