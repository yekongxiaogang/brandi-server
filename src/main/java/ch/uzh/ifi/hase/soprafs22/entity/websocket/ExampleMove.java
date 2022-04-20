package ch.uzh.ifi.hase.soprafs22.entity.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.Player;

public class ExampleMove {

    private Player player;
    private Card playedCard;
    private Long ballId;
    private int destinationTile;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public int getDestinationTile() {
        return destinationTile;
    }

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }
}
