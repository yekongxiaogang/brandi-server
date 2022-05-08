package ch.uzh.ifi.hase.soprafs22.rest.dto.DeckOfCardsAPI;

import java.util.List;

public class DrawCardsDTO {
    private List<CardDTO> cards;
    private Boolean success;

    public List<CardDTO> getCards() {
        return this.cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


}
