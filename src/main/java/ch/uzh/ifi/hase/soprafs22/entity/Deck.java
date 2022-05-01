package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private List<Card> cards = new ArrayList<>();

    public Deck(){
        newDeck();
    }

    private void newDeck(){
        this.cards.clear();
        for (GenerateRank aRank : GenerateRank.values()) {
            for (Suit aSuit : Suit.values()) {
                this.cards.add(new Card(Rank.valueOf(aRank.toString()), aSuit));
            }
        }
        Collections.shuffle(this.cards);
    }

    // Draw card, refill deck and draw if no cards in deck
    public Card drawCard(){
        try {
            return this.cards.remove(0);
        } catch (Exception e) {
            this.newDeck();
            return this.cards.remove(0);
        }
    }
}
