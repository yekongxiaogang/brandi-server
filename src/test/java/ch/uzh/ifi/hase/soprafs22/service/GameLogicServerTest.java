package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameLogicServerTest {
    @Mock
    private GameLogicService gameLogicService = new GameLogicService();

    @InjectMocks
    private Ball ball1= new Ball(Color.GREEN, -1);
    private Ball ball2= new Ball(Color.GREEN, 0);
    private Ball ball3 = new Ball(Color.GREEN, 64);
    private Ball ball4= new Ball(Color.GREEN, 14);
    private Ball ball5 = new Ball(Color.GREEN, 16);
    private Ball ball6 = new Ball(Color.GREEN, 63);


    @BeforeEach
    public void setup() {
//        MockitoAnnotations.openMocks(this);

//        Ball ball1 = new Ball(Color.GREEN, -1);
//        Ball ball2 = new Ball(Color.GREEN, 0);
//        Ball ball3 = new Ball(Color.GREEN, 64);

    }

    @Test
    public void cardChosen_validBalls() {
        Rank cardRank = Rank.ACE;
        Color playerColor = Color.GREEN;

        Set<Ball> balls = new HashSet<>(Set.of(ball1, ball2, ball3, ball4, ball5, ball6));

        Set<Integer> testHighlightedBalls = new HashSet<>(Set.of(-1, 0, 64, 14, 16, 63));

        Set<Integer> highlightedBalls = gameLogicService.highlightBalls(cardRank, balls, playerColor);

        assertEquals(testHighlightedBalls, highlightedBalls);
    }

    @Test
    public void ballChosen_validPossibleMoves() {
        Rank cardRank = Rank.ACE;

        Set<Ball> balls = new HashSet<>(Set.of(ball1, ball2, ball3, ball4, ball5, ball6));

        Set<Integer> testPossibleMoves = new HashSet<>(Set.of(1,11,100));

        Set<Integer> possibleMoves = gameLogicService.getPossibleMoves(cardRank, balls, ball2);

        assertEquals(testPossibleMoves, possibleMoves);

    }

//    @Test
//    public void ballChosen_checkBallOnStarting_validPossibleMoves() {
//
//        List<Ball> balls = new ArrayList<>(List.of(ball1, ball2, ball3, ball4, ball5));
//
//        List<Integer> providedPossibleMoves = new ArrayList<>(List.of(1,11));
//
//        List<Integer> testPossibleMoves = new ArrayList<>(List.of(1));
//
//        List<Integer> possibleMoves = gameLogicService.checkBallOnStarting(ball4, balls, providedPossibleMoves);
//
//        System.out.println(possibleMoves);
//        assertEquals(testPossibleMoves, possibleMoves);
//
//    }

    @Test
    public void ballChosen_validPossibleDestinations() {
    Rank cardRank = Rank.ACE;

    Set<Ball> balls = new HashSet<>(Set.of(ball1, ball2, ball3, ball4, ball5, ball6));

    Set<Integer> possibleMoves = new HashSet<>(Set.of(1, 11));

    Set<Integer> testPossibleDestinations1 = new HashSet<>(Set.of(1, 11));
    Set<Integer> testPossibleDestinations2 = new HashSet<>(Set.of(0, 10));
//    List<Integer> testPossibleMoves3 = new ArrayList<>(List.of(1, 11, 100));

    Set<Integer> possibleDestinations1 = gameLogicService.getPossibleDestinations(possibleMoves, ball2);
    Set<Integer> possibleDestinations2 = gameLogicService.getPossibleDestinations(possibleMoves, ball6);
//    List<Integer> possibleMoves3 = gameLogicService.getPossibleMoves(cardRank, balls, ball3);

    assertEquals(testPossibleDestinations1, possibleDestinations1);
    assertEquals(testPossibleDestinations2, possibleDestinations2);
    }

}
