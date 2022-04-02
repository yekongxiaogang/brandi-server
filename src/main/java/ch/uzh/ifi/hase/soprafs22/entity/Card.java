package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;

public class Card {
    private Rank rank;
    private Suit suit;

    public Card(Rank cardRank, Suit cardSuit){
        this.rank = cardRank;
        this.suit = cardSuit;
    }
    public Rank getRank() { return rank; }

    public Suit getSuit() { return suit; }

}
