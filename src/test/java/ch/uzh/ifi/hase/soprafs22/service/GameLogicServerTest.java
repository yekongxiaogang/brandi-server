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
    private final Ball green14 = new Ball(Color.GREEN, 14);
    private final Ball green16 = new Ball(Color.GREEN, 16);
    private final Ball green80 = new Ball(Color.GREEN, 80);
    private final Ball green81 = new Ball(Color.GREEN, 81);
    private final Ball green82 = new Ball(Color.GREEN, 82);
    private final Ball green83 = new Ball(Color.GREEN, 83);

    private final Ball green63 = new Ball(Color.GREEN, 63);
    private final Ball green64 = new Ball(Color.GREEN, 64);
    private final Ball green65 = new Ball(Color.GREEN, 65);
    private final Ball green66 = new Ball(Color.GREEN, 66);
    private final Ball green67 = new Ball(Color.GREEN, 67);

    private final Ball red84 = new Ball(Color.RED, 84);
    private final Ball red85 = new Ball(Color.RED, 85);
    private final Ball red86 = new Ball(Color.RED, 86);
    private final Ball red68 = new Ball(Color.RED, 68);
    private final Ball red15 = new Ball(Color.RED, 15);
    private final Ball red16 = new Ball(Color.RED, 16);
    private final Ball red17 = new Ball(Color.RED, 17);

    private final Ball yellow88 = new Ball(Color.YELLOW, 88);
    private final Ball yellow89 = new Ball(Color.YELLOW, 89);
    private final Ball yellow91 = new Ball(Color.YELLOW, 91);
    private final Ball yellow72 = new Ball(Color.YELLOW, 72);
    private final Ball yellow31 = new Ball(Color.YELLOW, 31);
    private final Ball yellow32 = new Ball(Color.YELLOW, 32);
    private final Ball yellow33 = new Ball(Color.YELLOW, 33);

    private final Ball blue47 = new Ball(Color.BLUE, 47);
    private final Ball blue48 = new Ball(Color.BLUE, 48);
    private final Ball blue49 = new Ball(Color.BLUE, 49);
    private final Ball blue92 = new Ball(Color.BLUE, 92);
    private final Ball blue93 = new Ball(Color.BLUE, 93);
    private final Ball blue94 = new Ball(Color.BLUE, 94);
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
    public void checkBallOnTheWayInBaseTest() {

        Set<Ball> balls = new HashSet<>(Set.of(green64, green66));

        Set<Integer> testPossibleMoves1 = new HashSet<>(Set.of(1,2,3));

        assertEquals(Set.of(1), gameLogicService.checkBallOnTheWayInBase(green64, balls, testPossibleMoves1));

    }

    @Test
    public void ballChosen_validPossibleDestinations() {
    // GOING INTO BASE WITH DIFFERENT COLORS
        Set<Integer> possibleDestinationsGREEN1 = gameLogicService.getPossibleDestinations(Set.of(5), green63, Set.of());
        Set<Integer> possibleDestinationsGREENinBase = gameLogicService.getPossibleDestinations(Set.of(1,2,3), green64, Set.of());

        Set<Integer> possibleDestinationsRED1 = gameLogicService.getPossibleDestinations(Set.of(5), red15, Set.of());
        Set<Integer> possibleDestinationsYELLOW1 = gameLogicService.getPossibleDestinations(Set.of(5), yellow31, Set.of());
        Set<Integer> possibleDestinationsBLUE1 = gameLogicService.getPossibleDestinations(Set.of(5), blue47, Set.of());

        assertEquals(Set.of(4,67), possibleDestinationsGREEN1);
        assertEquals(Set.of(20,71), possibleDestinationsRED1);
        assertEquals(Set.of(36,75), possibleDestinationsYELLOW1);
        assertEquals(Set.of(79,52), possibleDestinationsBLUE1);
        assertEquals(Set.of(65,66,67), possibleDestinationsGREENinBase);

    Set<Integer> possibleDestinations1 = gameLogicService.getPossibleDestinations(Set.of(1, 11), green0, Set.of());
    Set<Integer> possibleDestinations2 = gameLogicService.getPossibleDestinations(Set.of(1, 11), green63, Set.of());

    Set<Integer> possibleDestinationsGREEN = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), green80, Set.of());
    Set<Integer> possibleDestinationsRED = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), red84, Set.of());
    Set<Integer> possibleDestinationsYELLOW = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), yellow88, Set.of());
    Set<Integer> possibleDestinationsBLUE = gameLogicService.getPossibleDestinations(Set.of(1, 11, 100), blue95, Set.of());

    Set<Integer> possibleDestinationsGREEN4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), green0, Set.of());
    Set<Integer> possibleDestinationsRED4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), red16, Set.of());
    Set<Integer> possibleDestinationsYELLOW4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), yellow32, Set.of());
    Set<Integer> possibleDestinationsBLUE4 = gameLogicService.getPossibleDestinations(Set.of(-4, 4), blue48, Set.of());


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

        assertEquals(List.of(0,1,2,3,4,5,6), from0to6);
        assertEquals(List.of(62,63,0,1,2,3,4), from62to4);
        assertEquals(List.of(4,3,2,1,0), from4to0);
        assertEquals(List.of(63,0), from63to0);
        assertEquals(List.of(14,15), from14to15);
        assertEquals(List.of(14,15,16,17,18,19,20,21,22,23,24,25), from14to25);

    }

    @Test
    public void excludeTooLongMovesTest() {

        Set <Integer> possibleMoves = new HashSet<>(Set.of(-4,1,2,3,4,5,6,7,8,9,10,11,12,13));

        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(green64, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(red68, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(yellow72, possibleMoves));
        assertEquals(Set.of(1,2,3), gameLogicService.excludeTooLongMoves(blue76, possibleMoves));

    }

    @Test
    public void getFreeHomeHolesTest() {

        Set<Ball> balls = new HashSet<Ball>(Set.of(green81,green82,green83,
                red84,red85,red86,
                yellow88,yellow89,yellow91,
                blue92,blue93,blue94));

        Set<Ball> baseFull = new HashSet<Ball>(Set.of(green80,green81,green82,green83));

        assertEquals(Set.of(80), gameLogicService.getFreeHomeHoles(Color.GREEN, balls));
        assertEquals(Set.of(87), gameLogicService.getFreeHomeHoles(Color.RED, balls));
        assertEquals(Set.of(90), gameLogicService.getFreeHomeHoles(Color.YELLOW, balls));
        assertEquals(Set.of(95), gameLogicService.getFreeHomeHoles(Color.BLUE, balls));

        assertEquals(Set.of(), gameLogicService.getFreeHomeHoles(Color.GREEN, baseFull));

    }

    @Test
    public void ballBackToHomeTest() {

        Set<Ball> balls = new HashSet<Ball>(Set.of(green1,green81,green82,green83,
                red17,red84,red85,red86,
                yellow33,yellow88,yellow89,yellow91,
                blue49,blue92,blue93,blue94));

        gameLogicService.ballBackToHome(green1, balls);
        gameLogicService.ballBackToHome(red17, balls);
        gameLogicService.ballBackToHome(yellow33, balls);
        gameLogicService.ballBackToHome(blue49, balls);

        assertEquals(80, green1.getPosition());
        assertEquals(87, red17.getPosition());
        assertEquals(90, yellow33.getPosition());
        assertEquals(95, blue49.getPosition());

    }

}
