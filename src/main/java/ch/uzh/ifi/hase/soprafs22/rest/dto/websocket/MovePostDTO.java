package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Card;

public class MovePostDTO {

    private Long ballId;
    private int destinationTile;
    private Card playedCard;

    public int getDestinationTile() {
        return destinationTile;
    }

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }

    public Card getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(Card playedCard) {
        this.playedCard = playedCard;
    }

    public Long getBallId() {
        return ballId;
    }

    public void setBallId(Long ballId) {
        this.ballId = ballId;
    }

}
