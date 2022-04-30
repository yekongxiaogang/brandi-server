package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


@Entity
public class PlayerHand {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Card_id")
    private Set<Card> activeCards = new HashSet<>();

    public PlayerHand(){
        this.activeCards = new HashSet<>();
    }
    
    /* Delete a card from playerHand, used when card is played */
    public void deleteCard(Card cardToDelete){
        for(Card card : this.activeCards){
            if(card.getRank().equals(cardToDelete.getRank()) && card.getSuit().equals(cardToDelete.getSuit())){
                this.activeCards.remove(card);
            }
        }
    }

    /* Draw new cards, used at beginning of new round */
    public void drawCards(HashSet<Card> cards){
        if(!(cards == null)){
            try {
                this.activeCards = cards;
            } catch (Exception e) {
                System.out.println("Something went wrong while drawing cards:" + e);
            }
        } else{
            System.out.println("Array of cards passed to PlayerHand is null");
        }
    }


    public Set<Card> getActiveCards() {
        return this.activeCards;
    }

    public void setActiveCards(HashSet<Card> activeCards) {
        this.activeCards = activeCards;
    }

}
