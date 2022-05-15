package ch.uzh.ifi.hase.soprafs22.entity.websocket;

import java.util.ArrayList;
import java.util.List;

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

    private List<Long> ballIdsEliminated;
    private List<Integer> newPositions;

    public List<Integer> getNewPositions() {
        return this.newPositions;
    }

    public void setNewPositions(List<Integer> newPositions) {
        this.newPositions = newPositions;
    }

    public void addNewPositions(Integer position){
        if(this.newPositions == null) this.newPositions = new ArrayList<Integer>();
        this.newPositions.add(position);
    }

    public List<Long> getBallIdsEliminated() {
        return this.ballIdsEliminated;
    }

    public void setBallIdsEliminated(List<Long> ballIdsEliminated) {
        this.ballIdsEliminated = ballIdsEliminated;
    }

    public void addBallIdsEliminated(Long ballId){
        if(this.ballIdsEliminated == null) this.ballIdsEliminated = new ArrayList<Long>();
        this.ballIdsEliminated.add(ballId);
    }

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
