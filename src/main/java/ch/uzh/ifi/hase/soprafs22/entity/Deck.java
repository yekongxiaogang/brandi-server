package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs22.constant.GenerateRank;
import ch.uzh.ifi.hase.soprafs22.constant.Rank;
import ch.uzh.ifi.hase.soprafs22.constant.Suit;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Card> cards;

    public Deck(){
        this.cards = new HashSet<>();
        newDeck();
    }

    private void newDeck(){
        this.cards.clear();
        for (GenerateRank aRank : GenerateRank.values()) {
            for (Suit aSuit : Suit.values()) {
                this.cards.add(new Card(Rank.valueOf(aRank.toString()), aSuit));
            }
        }
        // Dont think we have to shuffle cards anymore since set is unordered
    }

    // Draw card, refill deck and draw if no cards in deck
    public Card drawCard(){
        try {
            Card card = this.cards.iterator().next();
            this.cards.remove(card);
            return card;
        } catch (Exception e) {
            this.newDeck();
            Card card = this.cards.iterator().next();
            this.cards.remove(card);
            return card;
        }
    }
}
