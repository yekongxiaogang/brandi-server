package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class BoardState {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ball> balls;

    public BoardState() {}

    public BoardState(ArrayList<Ball> balls) {
        this.balls = balls;
    }


    public List<Ball> getBalls() {
        return this.balls;
    }

}
