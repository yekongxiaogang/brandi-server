package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import java.util.List;

public class MoveGetDTO {

    private Long ballId;
    private Long targetBallId;
    private int destinationTile;
    private int[] holesTravelled;

    private int targetBallNewPosition;
    private Long cardId;

    private List<Long> ballIdsEliminated;
    private List<Integer> newPositions;

    public List<Integer> getNewPositions() {
        return this.newPositions;
    }

    public void setNewPositions(List<Integer> newPositions) {
        this.newPositions = newPositions;
    }
    
    public List<Long> getBallIdsEliminated() {
        return this.ballIdsEliminated;
    }

    public void setBallIdsEliminated(List<Long> ballIdsEliminated) {
        this.ballIdsEliminated = ballIdsEliminated;
    }

    public Long getBallId() {
        return ballId;
    }

    public void setBallId(Long ballId) {
        this.ballId = ballId;
    }

    public Long getTargetBallId() {return targetBallId;}

    public void setTargetBallId(Long targetBallId) {this.targetBallId = targetBallId;}

    public int getDestinationTile() {
        return destinationTile;
    }

    public void setDestinationTile(int destinationTile) {
        this.destinationTile = destinationTile;
    }

    public int[] getHolesTravelled() {return holesTravelled;}

    public void setHolesTravelled(int[] holesTravelled) {this.holesTravelled = holesTravelled;}

    public int getTargetBallNewPosition() {return targetBallNewPosition;}

    public void setTargetBallNewPosition(int targetBallNewPosition) {this.targetBallNewPosition = targetBallNewPosition;}

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
