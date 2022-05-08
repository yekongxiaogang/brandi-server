package ch.uzh.ifi.hase.soprafs22.rest.dto.DeckOfCardsAPI;

public class DeckDTO {
    private Boolean success;
    private String deck_id;
    private int remaining;
    private Boolean shuffled;

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getDeck_id() {
        return this.deck_id;
    }

    public void setDeck_id(String deck_id) {
        this.deck_id = deck_id;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public Boolean getShuffled() {
        return this.shuffled;
    }

    public void setShuffled(Boolean shuffled) {
        this.shuffled = shuffled;
    }

}
