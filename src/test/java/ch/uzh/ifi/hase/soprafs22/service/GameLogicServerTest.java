package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.entity.Ball;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicServerTest {
    @Mock
    private GameLogicService gameLogicService = new GameLogicService();

    @InjectMocks
    private final Ball green1 = new Ball(Color.GREEN, 1);
    private final Ball green0 = new Ball(Color.GREEN, 0);
    private final Ball green64 = new Ball(Color.GREEN, 64);
    private final Ball green14 = new Ball(Color.GREEN, 14);
    private final Ball green16 = new Ball(Color.GREEN, 16);
    private final Ball green80 = new Ball(Color.GREEN, 80);
    private final Ball green63 = new Ball(Color.GREEN, 63);
    private final Ball green67 = new Ball(Color.GREEN, 67);

    private final Ball red84 = new Ball(Color.RED, 84);
    private final Ball red68 = new Ball(Color.RED, 68);
    private final Ball red15 = new Ball(Color.RED, 15);
    private final Ball red16 = new Ball(Color.RED, 16);

    private final Ball yellow88 = new Ball(Color.YELLOW, 88);
    private final Ball yellow72 = new Ball(Color.YELLOW, 72);
    private final Ball yellow31 = new Ball(Color.YELLOW, 31);
    private final Ball yellow32 = new Ball(Color.YELLOW, 32);

    private final Ball blue48 = new Ball(Color.BLUE, 48);
    private final Ball blue47 = new Ball(Color.BLUE, 47);
    private final Ball blue95 = new Ball(Color.BLUE, 95);
    private final Ball blue76 = new Ball(Color.BLUE, 76);

    @Test
    public void cardChosen_validBalls() {
        Rank cardRank = Rank.ACE;
        Color playerColor = Color.GREEN;

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16, green63, green67));

        assertEquals(Set.of(1, 0, 64, 14, 16), gameLogicService.highlightBalls(cardRank, balls, playerColor));
    }

    @Test
    public void ballChosen_validPossibleMoves() {
        Rank cardRank1 = Rank.ACE;
        Rank cardRank2 = Rank.FOUR;

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green64, green14, green16, green63));

        Set<Integer> possibleMoves1 = gameLogicService.getPossibleMoves(cardRank1, balls, green0);
        Set<Integer> possibleMoves2 = gameLogicService.getPossibleMoves(cardRank2, balls, green0);

        assertEquals(Set.of(1,11), possibleMoves1);
        assertEquals(Set.of(4,-4), possibleMoves2);

    }

    @Test
    public void ballChosen_checkBallOnTheWayOnStarting_validPossibleMoves() {

        Set<Ball> balls = new HashSet<>(Set.of(green1, green0, green63, green14, red16));

        Set<Integer> testPossibleMoves1 = new HashSet<>(Set.of(1,11));

        assertEquals(Set.of(1), gameLogicService.checkBallOnTheWayOnStarting(green14, balls, testPossibleMoves1));
        assertEquals(Set.of(), gameLogicService.checkBallOnTheWayOnStarting(green63, balls, testPossibleMoves1));

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

    Set<Integer> possibleDestinationsGREEN4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), green0);
    Set<Integer> possibleDestinationsRED4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), red16);
    Set<Integer> possibleDestinationsYELLOW4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), yellow32);
    Set<Integer> possibleDestinationsBLUE4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), blue48);


    assertEquals(Set.of(1,11), possibleDestinations1);
    assertEquals(Set.of(0,10), possibleDestinations2);

    assertEquals(Set.of(4,60), possibleDestinationsGREEN4);
    assertEquals(Set.of(20,12), possibleDestinationsRED4);
    assertEquals(Set.of(36,28), possibleDestinationsYELLOW4);
    assertEquals(Set.of(52,44), possibleDestinationsBLUE4);

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

        List<Integer> from0to6 = gameLogicService.getHolesTravelled(6,0,true);
        List<Integer> from62to4 = gameLogicService.getHolesTravelled(4,62,true);
        List<Integer> from4to0 = gameLogicService.getHolesTravelled(0,4,true);
        List<Integer> from63to0 = gameLogicService.getHolesTravelled(0,63,true);
        List<Integer> from14to15 = gameLogicService.getHolesTravelled(15,14,true);
        List<Integer> from14to25 = gameLogicService.getHolesTravelled(25,14,true);

        assertEquals(List.of(1,2,3,4,5,6), from0to6);
        assertEquals(List.of(63,0,1,2,3,4), from62to4);
        assertEquals(List.of(3,2,1,0), from4to0);
        assertEquals(List.of(0), from63to0);
        assertEquals(List.of(15), from14to15);
        assertEquals(List.of(15,16,17,18,19,20,21,22,23,24,25), from14to25);

    }

    @Test
    public void excludeTooLongMovesTest() {

        Set <Integer> possibleMoves = new HashSet<>(Set.of(-4,1,2,3,4,5,6,7,8,9,10,11,12,13));

        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(green64, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(red68, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(yellow72, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(blue76, possibleMoves));

    }

}
