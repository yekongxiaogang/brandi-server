package ch.uzh.ifi.hase.soprafs22.rest.dto.DeckOfCardsAPI;

public class CardDTO {
    private String value;
    private String suit;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

}
