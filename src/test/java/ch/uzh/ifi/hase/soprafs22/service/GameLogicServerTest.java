package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameLogicServerTest {
    @Mock
    private GameLogicService gameLogicService = new GameLogicService();

    @InjectMocks
    private Ball green1 = new Ball(Color.GREEN, 1);
    private Ball green0 = new Ball(Color.GREEN, 0);
    private Ball green64 = new Ball(Color.GREEN, 64);
    private Ball green14 = new Ball(Color.GREEN, 14);
    private Ball green16 = new Ball(Color.GREEN, 16);
    private Ball green80 = new Ball(Color.GREEN, 80);
    private Ball green63 = new Ball(Color.GREEN, 63);

    private Ball red84 = new Ball(Color.RED, 84);
    private Ball red15 = new Ball(Color.RED, 15);

    private Ball yellow88 = new Ball(Color.YELLOW, 88);
    private Ball yellow31 = new Ball(Color.YELLOW, 31);

    private Ball blue47 = new Ball(Color.BLUE, 47);
    private Ball blue95 = new Ball(Color.BLUE, 95);


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

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16, green63));

        Set<Integer> testHighlightedBalls = new HashSet<>(Set.of(1, 0, 64, 14, 16, 63));

        Set<Integer> highlightedBalls = gameLogicService.highlightBalls(cardRank, balls, playerColor);

        assertEquals(testHighlightedBalls, highlightedBalls);
    }

    @Test
    public void ballChosen_validPossibleMoves() {
        Rank cardRank1 = Rank.ACE;
        Rank cardRank2 = Rank.FOUR;

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16, green63));

        Set<Integer> testPossibleMoves1 = new HashSet<>(Set.of(1,11));
        Set<Integer> testPossibleMoves2 = new HashSet<>(Set.of(4,-4));

        Set<Integer> possibleMoves1 = gameLogicService.getPossibleMoves(cardRank1, balls, green0);
        Set<Integer> possibleMoves2 = gameLogicService.getPossibleMoves(cardRank2, balls, green0);

        assertEquals(testPossibleMoves1, possibleMoves1);
        assertEquals(testPossibleMoves2, possibleMoves2);

    }

    @Test
    public void ballChosen_checkBallOnStarting_validPossibleMoves() {

        // BallPos 1, 0, 64, 14, 16
        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16));

        Set<Integer> providedPossibleMoves = new HashSet<>(Set.of(1,11));

        // FIXME
        Set<Integer> testPossibleMoves = new HashSet<>(Set.of(1));

        Set<Integer> possibleMoves = gameLogicService.checkBallOnStarting(green64, balls, providedPossibleMoves);

//        assertEquals(testPossibleMoves, possibleMoves);

    }

    @Test
    public void ballChosen_validPossibleDestinations() {
    // GOING INTO BASE WITH DIFFERENT COLORS
        Set<Integer> possibleDestinationsGREEN1 = gameLogicService.getPossibleDestinations(Set.of(5), green63);
        Set<Integer> possibleDestinationsGREENinBase = gameLogicService.getPossibleDestinations(Set.of(1,2,3), green64);

        Set<Integer> possibleDestinationsRED1 = gameLogicService.getPossibleDestinations(Set.of(5), red15);
        Set<Integer> possibleDestinationsYELLOW1 = gameLogicService.getPossibleDestinations(Set.of(5), yellow31);
        Set<Integer> possibleDestinationsBLUE1 = gameLogicService.getPossibleDestinations(Set.of(5), blue47);

        assertEquals(Set.of(4,67), possibleDestinationsGREEN1);
        assertEquals(Set.of(20,71), possibleDestinationsRED1);
        assertEquals(Set.of(36,75), possibleDestinationsYELLOW1);
        assertEquals(Set.of(79,52), possibleDestinationsBLUE1);
        assertEquals(Set.of(65,66,67), possibleDestinationsGREENinBase);

    Set<Integer> possibleDestinations1 = gameLogicService.getPossibleDestinations(Set.of(1, 11), green0);
    Set<Integer> possibleDestinations2 = gameLogicService.getPossibleDestinations(Set.of(1, 11), green63);

    Set<Integer> possibleDestinationsGREEN = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), green80);
    Set<Integer> possibleDestinationsRED = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), red84);
    Set<Integer> possibleDestinationsYELLOW = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), yellow88);
    Set<Integer> possibleDestinationsBLUE = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), blue95);

    Set<Integer> possibleDestinations3 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), green0);

    assertEquals(Set.of(1, 11), possibleDestinations1);
    assertEquals(Set.of(0, 10), possibleDestinations2);
    assertEquals(Set.of(4, 60), possibleDestinations3);

    assertEquals(Set.of(0), possibleDestinationsGREEN);
    assertEquals(Set.of(16), possibleDestinationsRED);
    assertEquals(Set.of(32), possibleDestinationsYELLOW);
    assertEquals(Set.of(48), possibleDestinationsBLUE);
    }

    @Test
    public void checkCanGoOutOfHomeTest() {

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16, green63, green80, red84, yellow88, blue95));

        assertEquals(0, gameLogicService.checkCanGoOutOfHome(green0, balls));
        assertEquals(1, gameLogicService.checkCanGoOutOfHome(green80, balls));
        assertEquals(2, gameLogicService.checkCanGoOutOfHome(red84, balls));
        assertEquals(2, gameLogicService.checkCanGoOutOfHome(yellow88, balls));
        assertEquals(2, gameLogicService.checkCanGoOutOfHome(blue95, balls));
    }

    @Test
    public void getHolesTravelledTest() {

        List<Integer> from0to6 = gameLogicService.getHolesTravelled(6,0);
        List<Integer> from62to4 = gameLogicService.getHolesTravelled(4,62);
        List<Integer> from4to0 = gameLogicService.getHolesTravelled(0,4);

        assertEquals(List.of(1,2,3,4,5,6), from0to6);
        assertEquals(List.of(63,0,1,2,3,4), from62to4);
        assertEquals(List.of(3,2,1,0), from4to0);

    }

}
