package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.BallState;
import ch.uzh.ifi.hase.soprafs22.constant.Color;

public class Ball {
    private Color color;
    private Integer ballNum;
    private Integer position;
    private BallState state;


    public Ball(Color color, Integer ballNum, Integer position, BallState state) {
        this.color = color;
        this.ballNum = ballNum;
        this.position = position;
        this.state = state;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BallState getState() {
        return this.state;
    }

    public void setState(BallState state) {
        this.state = state;
    }

}
