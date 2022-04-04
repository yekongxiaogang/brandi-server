package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class BoardState {

    @Id
    @GeneratedValue
    /* @OneToOne(targetEntity = Game.class)
    @JoinColumn(name = "Boardstate_id") */
    private Long id;

    // @ElementCollection
    @OneToMany
    private List<Ball> balls;


    public BoardState(ArrayList<Ball> balls) {
        this.balls = balls;
    }


    public List<Ball> getBalls() {
        return this.balls;
    }

}
