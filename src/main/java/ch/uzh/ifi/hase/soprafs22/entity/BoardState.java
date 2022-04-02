package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

public class BoardState {
    private ArrayList<Ball> balls;


    public BoardState(ArrayList<Ball> balls) {
        this.balls = balls;
    }


    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

}
