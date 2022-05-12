package ch.uzh.ifi.hase.soprafs22.entity.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class Move {

    private User user;
    private Card playedCard;
    private Long ballId;
    private int destinationTile;

    private Long targetedBallId;
    private int targetedBallPosition;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Long getTargetedBallId() {return targetedBallId;}

    public void setTargetedBallId(Long targetedBallId) {this.targetedBallId = targetedBallId;}

    public int getDestinationTile() {
        return destinationTile;
    }

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }

    public int getTargetedBallPosition() {
        return targetedBallPosition;
    }

    public void setTargetedBallPosition(int targetedBallPosition) {
        this.targetedBallPosition = targetedBallPosition;
    }
}
