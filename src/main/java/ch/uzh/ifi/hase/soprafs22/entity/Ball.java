package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;

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

    public Ball() {}

    public Ball(Color color, Integer position) {
        this.color = color;
        this.position = position;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }

    public Boolean checkBallInBase() {
        if (BoardState.basePoints.contains(this.getPosition())) return true;
        return false;
    }

    public Boolean checkBallInHome() {
        if (BoardState.homePoints.contains(this.getPosition())) return true;
        return false;
    }

}
