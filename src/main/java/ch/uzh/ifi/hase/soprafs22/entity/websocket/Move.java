package ch.uzh.ifi.hase.soprafs22.entity.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Card;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class Move {

    private User user;
    private Card playedCard;
    private Long ballId;
    private int destinationTile;
    private int index;

    private int[] holesTravelled;

    private Long targetBallId;
    private int targetBallNewPosition;


    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
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

    public Long getTargetBallId() {return targetBallId;}

    public void setTargetBallId(Long targetBallId) {this.targetBallId = targetBallId;}

    public int getDestinationTile() {return destinationTile;}

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }

    public Long getCardId(){
        return this.playedCard.getId();
    }
    public int[] getHolesTravelled() {return holesTravelled;}

    public void setHolesTravelled(int[] holesTravelled) {this.holesTravelled = holesTravelled;}

    public int getTargetBallNewPosition() {return targetBallNewPosition;}

    public void setTargetBallNewPosition(int targetBallNewPosition) {this.targetBallNewPosition = targetBallNewPosition;}
}
