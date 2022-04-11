package ch.uzh.ifi.hase.soprafs22.rest.dto.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Player;

public class ExampleMoveGetDTO {

    private Player player;
    private Long ballId;
    private int destinationTile;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
