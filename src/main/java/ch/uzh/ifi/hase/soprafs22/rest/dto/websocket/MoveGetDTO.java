package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

public class MoveGetDTO {

    private Long ballId;
    private Long targetBallId;
    private int destinationTile;

    private int targetBallPosition;
    private Long cardId;

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

    public int getTargetBallPosition() {return targetBallPosition;}

    public void setTargetBallPosition(int targetBallPosition) {this.targetBallPosition = targetBallPosition;}

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
