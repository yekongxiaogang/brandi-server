package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;

@Entity
public class Card {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private Rank rank;

    @Column(nullable=false)
    private Suit suit;

    public Card(){}

    public Card(Rank cardRank, Suit cardSuit){
        this.rank = cardRank;
        this.suit = cardSuit;
    }

    public Rank getRank() { return rank; }

    public Suit getSuit() { return suit; }

}
