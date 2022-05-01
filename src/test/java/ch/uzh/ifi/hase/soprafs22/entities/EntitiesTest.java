package ch.uzh.ifi.hase.soprafs22.entities;

import ch.uzh.ifi.hase.soprafs22.constant.Color;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;
import ch.uzh.ifi.hase.soprafs22.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntitiesTest {

    @Test
    public void BallTest() {

        Ball ball = new Ball(Color.GREEN, 0);

        assertEquals(0, ball.getPosition());
        ball.setPosition(1);
        assertEquals(1, ball.getPosition());

        assertEquals(null, ball.getId());
        assertEquals(Color.GREEN, ball.getColor());

    }

    @Test
    public void BoardStateTest() {

        Ball ball = new Ball(Color.GREEN, 0);

        Set<Ball> balls = new HashSet<>(Set.of(ball));

        BoardState boardState = new BoardState(balls);

        assertEquals(balls, boardState.getBalls());
        //assertEquals(null, boardState.getBallById(null));

    }

    @Test
    public void CardTest() {

        Card card = new Card(Rank.ACE, Suit.CLUB);

        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.CLUB, card.getSuit());

    }

    @Test
    public void DeckTest() {

        Card card = new Card(Rank.ACE, Suit.CLUB);

        Deck deck = new Deck();

        for (int i = 0; i <= 52; i++) {
            if (deck.drawCard().equals(card)) {
                assert(deck.drawCard() instanceof Card);
            }
        }

    }

    @Test
    public void GameTurnTest() {

        PlayerState activePlayer = new PlayerState();

        BoardState boardState = new BoardState();

        GameTurn gameTurn = new GameTurn(activePlayer, boardState);
    }

    @Test
    public void PlayerHandTest() {

        PlayerHand playerHand = new PlayerHand();

        Card card = new Card(Rank.ACE, Suit.CLUB);

        HashSet<Card> cards = new HashSet<>(Set.of(card));
        HashSet<Card> cardsEmpty = new HashSet<>(Set.of());

        playerHand.drawCards(cards);
        assertEquals(cards, playerHand.getActiveCards());

        playerHand.deleteCard(card);
        assertEquals(cardsEmpty, playerHand.getActiveCards());

    }

    @Test
    public void PlayerStateTest() {

        User player = new User();
        PlayerHand playerHand = new PlayerHand();

        Card card = new Card(Rank.ACE, Suit.CLUB);

        HashSet<Card> cards = new HashSet<>(Set.of(card));

        PlayerState playerState = new PlayerState(player, 1, Color.GREEN, true, playerHand);

        assertEquals(false, playerState.getIsPlaying());
        playerState.setIsPlaying(true);
        assertEquals(true, playerState.getIsPlaying());

        assertEquals(1, playerState.getTeam());
        playerState.setTeam(2);
        assertEquals(2, playerState.getTeam());

        assertEquals(true, playerState.getPlayerStatus());
        playerState.setPlayerStatus(false);
        assertEquals(false, playerState.getPlayerStatus());

        assertEquals(Color.GREEN, playerState.getColor());
        playerState.setColor(Color.RED);
        assertEquals(Color.RED, playerState.getColor());

        assertEquals(playerHand, playerState.getPlayerHand());
        playerState.drawCards(cards);
        assertEquals(playerHand, playerState.getPlayerHand());

    }

}
