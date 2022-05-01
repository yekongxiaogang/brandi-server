package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import ch.uzh.ifi.hase.soprafs22.entity.BoardState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class GameLogicService {

    public Set<Integer> highlightBalls (Rank cardRank, Set<Ball> balls, Color playerColor) {
        Set<Integer> highlightedBalls = new HashSet<>();

        for (Ball ball : balls) {
            int ballPos = ball.getPosition();
            if (ball.getColor().equals(playerColor)) {
                if (!(BoardState.homePoints.contains(ball.getPosition()))) {
                    highlightedBalls.add(ballPos);
                }
                else if (cardRank.equals(Rank.ACE) || (cardRank.equals(Rank.KING))){
                    highlightedBalls.add(ballPos);
                }
            }
        }

        return highlightedBalls;
    }

    public Set<Integer> getPossibleMoves(Rank cardRank, Set<Ball> balls, Ball ball) {
        Set<Integer> possibleMoves = new HashSet<Integer>();

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

            if (checkCanGoOutOfHome(ball, balls)) {
                possibleMoves.add(100);
            }
        }
        else if (cardRank.equals(Rank.ACE)) {
            possibleMoves.add(1);
            possibleMoves.add(11);

            if (checkCanGoOutOfHome(ball, balls)) {
                possibleMoves.add(100);
            }
        }

        return possibleMoves;
    }

    public Set<Integer> getPossibleDestinations (Set<Integer> possibleMoves, Ball ball) {

        Set<Integer> possibleDestinations = new HashSet<Integer>();

        for (int possibleMove : possibleMoves) {
            if (possibleMove == 100) {

                if (ball.getColor().equals(Color.GREEN)) {
                    possibleDestinations = Set.of(0);
                }
                else if (ball.getColor().equals(Color.RED)) {
                    possibleDestinations = Set.of(16);
                }
                else if (ball.getColor().equals(Color.YELLOW)) {
                    possibleDestinations = Set.of(32);
                }
                else {
                    possibleDestinations = Set.of(48);
                }

                break;
            }
            // modulo div as board's last pos is 63
            if (!((ball.getPosition() + possibleMove) < 0)) {
                possibleDestinations.add((ball.getPosition() + possibleMove) % 64);
            }
            else {
                possibleDestinations.add((ball.getPosition() + possibleMove) + 64);
            }

        }

        return possibleDestinations;
    }

    public Set<Integer> checkBallOnStarting (Ball ball, Set<Ball> balls, Set<Integer> possibleMoves) {

        int startPos = ball.getPosition();

        // for every possible move, we check if any ball on the way is on the starting point
        Set <Integer> toBeRemoved = new HashSet<>();
        for (Ball b : balls) {
            if (BoardState.startingPoints.contains(b.getPosition())) {
                for (int possibleMove : possibleMoves) {
                    for (int i = startPos + 1; i <= startPos + possibleMove; i++) {
                        if (b.getPosition().equals(i)) {
                            toBeRemoved.add(possibleMove);
                        }
//                        System.out.println(toBeRemoved + "" + i);
                    }
                }
            }
        }

        for (int i : toBeRemoved) {
            possibleMoves.remove(i);
        }

        return possibleMoves;
    }

    // Compensate for the offset in the hole number by adding specific value
    public Boolean checkCanGoBase (Color color, int position, Set<Integer> possibleMoves) {
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

    public Boolean checkCanGoOutOfHome (Ball ball, Set<Ball> balls) {

        Color color = ball.getColor();

        for (Ball b : balls) {
            if (color == b.getColor()) {
                if (color == Color.GREEN) {
                    if (b.getPosition().equals(0)) {
                        return false;
                    }
                }
                else if (color == Color.RED) {
                    if (b.getPosition().equals(16)) {
                        return false;
                    }
                }
                else if (color == Color.YELLOW) {
                    if (b.getPosition().equals(32)) {
                        return false;
                    }
                }
                else if (color == Color.BLUE) {
                    if (b.getPosition().equals(48)) {
                        return false;
                    }
                }
            }
        }
        return true;

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
