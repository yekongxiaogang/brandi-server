package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs22.constant.BallState;
import ch.uzh.ifi.hase.soprafs22.constant.Color;

@Entity
public class Ball {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private Color color;
    
    @Column(nullable = false)
    private Integer position;
    
    @Column(nullable = false)
    private BallState state;

    public Ball() {}

    // EDIT: When initializing a ball we want it to be in base and have pos = -1
    public Ball(Color color) {
        this.color = color;
        this.position = -1;
        this.state = BallState.BASE;
    }

    // position = -1 means ball still in base, not played yet
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

    public Long getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }



}
