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

            if (checkCanGoOutOfHome(ball, balls) == 2) {possibleMoves.add(100);}
            else if (checkCanGoOutOfHome(ball, balls) == 1) {possibleMoves = Set.of();}
            else {possibleMoves = Set.of(13);}
        }
        else if (cardRank.equals(Rank.ACE)) {

            if (checkCanGoOutOfHome(ball, balls) == 2) {possibleMoves.add(100);}
            else if (checkCanGoOutOfHome(ball, balls) == 1) {possibleMoves = Set.of();}
            else {possibleMoves = Set.of(1,11);}
        }

        return possibleMoves;
    }

    public Set<Integer> getStartPosition(Ball ball) {

        if (ball.getColor().equals(Color.GREEN)) {
            return Set.of(0);
        }
        else if (ball.getColor().equals(Color.RED)) {
            return Set.of(16);
        }
        else if (ball.getColor().equals(Color.YELLOW)) {
            return Set.of(32);
        }
        else {
            return Set.of(48);
        }
    }

    public Set<Integer> getPossibleDestinations (Set<Integer> possibleMoves, Ball ball) {

        Set<Integer> possibleDestinations = new HashSet<Integer>();

        for (int possibleMove : possibleMoves) {

            // CHECK IF BALL CAN GO OUT OF HOME
            if (possibleMove == 100) {
                possibleDestinations = getStartPosition(ball);
                break;
            }

            // CHECK IF BALL CAN GO BASE; IF SO ADD ADEQUATE DESTINATION
            Color color;
            Integer ballPos;
            if (checkCanGoBase(color = ball.getColor(), ballPos = ball.getPosition(), possibleMoves)) {

                int baseMove = ballPos + possibleMove;

                if (color.equals(Color.GREEN)) {
                    if (baseMove <= 67 && baseMove >= 64) {
                        possibleDestinations.add(baseMove);
                    }
                }
                else if (color.equals(Color.RED)) {
                    baseMove = ballPos + possibleMove + 51;
                    if (baseMove <= 71 && baseMove >= 68) {
                        possibleDestinations.add(baseMove);
                    }
                }
                else if (color.equals(Color.YELLOW)) {
                    baseMove = ballPos + possibleMove + 39;
                    if (baseMove <= 75 && baseMove >= 72) {
                        possibleDestinations.add(baseMove);
                    }
                }
                else {
                    baseMove = ballPos + possibleMove + 27;
                    if (baseMove <= 79 && baseMove >= 76) {
                        possibleDestinations.add(baseMove);
                    }
                }
            }

            // IF BALL ALREADY IN BASE DISABLE GOING BACK ONTO BOARD
            if (!ball.checkBallInBase(ball)) {

                // ADD POSSIBLE ON BOARD MOVES i.e. not into base/from home
                // modulo div as board's last pos is 63
                if (!((ball.getPosition() + possibleMove) < 0)) {
                    possibleDestinations.add((ball.getPosition() + possibleMove) % 64);
                }
                else {
                    possibleDestinations.add((ball.getPosition() + possibleMove) + 64);
                }

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
        else if (color.equals(Color.RED)) {
            for (int possibleMove : possibleMoves) {
                if (position + 51 + possibleMove <= 71) {
                    return true;
                }
            }
        }
        else if (color.equals(Color.YELLOW)) {
            for (int possibleMove : possibleMoves) {
                if (position + 39 + possibleMove <= 75) {
                    return true;
                }
            }
        }
        else if (color.equals(Color.BLUE)) {
            for (int possibleMove : possibleMoves) {
                if (position + 27 + possibleMove <= 79) {
                    return true;
                }
            }
        }
        return false;
    }

    // RETURNS 2 IF CAN GO OUT OF HOME, 1 IF START IS OCCUPIED, 0 IF BALL NOT IN HOME
    public int checkCanGoOutOfHome (Ball ball, Set<Ball> balls) {

        Color color = ball.getColor();

        if (!BoardState.homePoints.contains(ball.getPosition())) {
            return 0;
        }

        for (Ball b : balls) {
            if (color == b.getColor()) {
                if (color == Color.GREEN) {
                    if (b.getPosition().equals(0)) {
                        return 1;
                    }
                }
                else if (color == Color.RED) {
                    if (b.getPosition().equals(16)) {
                        return 1;
                    }
                }
                else if (color == Color.YELLOW) {
                    if (b.getPosition().equals(32)) {
                        return 1;
                    }
                }
                else if (color == Color.BLUE) {
                    if (b.getPosition().equals(48)) {
                        return 1;
                    }
                }
            }
        }
        return 2;

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
