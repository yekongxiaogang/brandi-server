package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;

public class MoveGetDTO {

    private User user;
    private Long ballId;
    private int destinationTile;

    public UserGetDTO getUser() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    public void setUser(User user) {
        this.user = user;
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
