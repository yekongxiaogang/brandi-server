package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import ch.uzh.ifi.hase.soprafs22.entity.BoardState;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GameLogicService {

    public List<Integer> highlightBalls (Rank cardRank, List<Ball> balls, Color playerColor) {
        List<Integer> highlightedBalls = new ArrayList<Integer>();

        for (Ball ball : balls) {
            int ballPos = ball.getPosition();
            if (ball.getColor().equals(playerColor)) {
                if (!ball.getPosition().equals(-1)) {
                    highlightedBalls.add(ballPos);
                }
                else {
                    if (cardRank.equals(Rank.ACE) || (cardRank.equals(Rank.KING))) {
                        highlightedBalls.add(ballPos);
                    }
                }
            }
        }

        return highlightedBalls;
    }

    public List<Integer> getPossibleMoves(Rank cardRank, List<Ball> balls, Ball ball) {
        List<Integer> possibleMoves = new ArrayList<Integer>();

        if (BoardState.normalCards.get(cardRank) != null) {
            possibleMoves.add(BoardState.normalCards.get(cardRank));
        }
        else if (cardRank.equals(Rank.FOUR)) {
            possibleMoves.add(4);
            possibleMoves.add(-4);
        }
        // TODO: implement cooperation with team member
        // and killing on the way
        // probably just a special number for the move
        // which then will be processed as for loop of 1 distance moves
        else if (cardRank.equals(Rank.SEVEN)) {
            // special move num
            possibleMoves.add(107);

            possibleMoves.add(1);
            possibleMoves.add(2);
            possibleMoves.add(3);
            possibleMoves.add(4);
            possibleMoves.add(5);
            possibleMoves.add(6);
            possibleMoves.add(7);
        }
        else if (cardRank.equals(Rank.JACK)) {
            // EXCHANGE TWO BALLS
            // A marble positioned for the first time at the start, at home or in the
            // target area, may not be exchanged.
            for (Ball b : balls) {
                int ballPos = ball.getPosition();
                if (!BoardState.startingPoints.contains(ballPos) && ballPos >= 0 && ballPos <= 63 ) {
                    possibleMoves.add(ball.getPosition());
                }
            }
        }
        else if (cardRank.equals(Rank.KING)) {
            // MOVE BY 13 OR INVOKE A BALL FROM HOME
            possibleMoves.add(13);

            // FIXME: 100 as invoke
            possibleMoves.add(100);
            //setInvokeTrue();
        }
        else if (cardRank.equals(Rank.ACE)) {
            possibleMoves.add(1);
            possibleMoves.add(11);

            possibleMoves.add(100);
            //setInvokeTrue();
        }

        // Remove duplicates from possible moves
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(possibleMoves);
        possibleMoves.clear();
        possibleMoves.addAll(set);

        return possibleMoves;
    }

    public List<Integer> getPossibleDestinations (List<Integer> possibleMoves, Ball ball) {

        List<Integer> possibleDestinations = new ArrayList<Integer>();

        for (int possibleMove : possibleMoves) {
            // modulo div as board's last pos is 63
            possibleDestinations.add((ball.getPosition() + possibleMove) % 64);
        }

        // TODO: PLAYERSTATE COULD MAYBE HAVE ITS BALLS?
//        for (Ball b: playerBalls) {
//            if (b.getColor().equals(playerColor)) {
//                for (int possibleMove : possibleMoves) {
//                    // modulo div as board's last pos is 63
//                    possibleDestinations.add((b.getPosition() + possibleMove) % 63);
//                }
//            }
//        }

        // Remove duplicates from possible destinations
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(possibleDestinations);
        possibleDestinations.clear();
        possibleDestinations.addAll(set);

        return possibleDestinations;
    }

    public List<Integer> checkBallOnStarting (Ball ball, List<Ball> balls, List<Integer> possibleMoves) {

        int startPos = ball.getPosition();

        // for every possible move, we check if any ball on the way is on the starting point
        List <Integer> toBeRemoved = new ArrayList<Integer>();
        for (Ball b : balls) {
            if (BoardState.startingPoints.contains(b.getPosition())) {
                for (int possibleMove : possibleMoves) {
                    for (int i = startPos + 1; i <= startPos + possibleMove; i++) {
                        toBeRemoved.add(possibleMove);
                    }
                }
            }
        }

        for (int i : toBeRemoved) {
            possibleMoves.remove(Integer.valueOf(i));
        }

        return possibleMoves;
    }

    // Compensate for the offset in the hole number by adding specific value
    public Boolean checkCanGoBase (Color color, int position, List<Integer> possibleMoves) {
        if (color.equals(Color.GREEN)) {
            for (int possibleMove : possibleMoves) {
                if (position + possibleMove <= 67) {
                    return true;
                }
            }
        }
        else if (color.equals(Color.BLUE)) {
            for (int possibleMove : possibleMoves) {
                if (position + 28 + possibleMove <= 79) {
                    return true;
                }
            }
        }
        else if (color.equals(Color.RED)) {
            for (int possibleMove : possibleMoves) {
                if (position + 52 + possibleMove <= 71) {
                    return true;
                }
            }
        }
        else if (color.equals(Color.YELLOW)) {
            for (int possibleMove : possibleMoves) {
                if (position + 40 + possibleMove <= 75) {
                    return true;
                }
            }
        }
        return false;
    }

    // Account for the case when move is made with 7 => player gets access to teammate's balls
    public List<Ball> getPlayerBalls (List<Ball> balls, List<Color> playerColors) {

        List<Ball> playerBalls = new ArrayList<Ball>();

        for (Color color : playerColors) {
            for (Ball ball : balls) {
                if (ball.getColor().equals(color)) {
                    playerBalls.add(ball);
                }
            }
        }

        return playerBalls;
    }

}
