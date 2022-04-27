package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class PlayerHand {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "Card_id")
    private List<Card> activeCards = new ArrayList<>();

    public PlayerHand(){
        this.activeCards = new ArrayList<Card>();
    }
    
    /* Delete a card from playerHand, used when card is played */
    public void deleteCard(Card cardToDelete){
        for(Card card : this.activeCards){
            // TODO: Implement equals() & hashCode() in Card to make this cleaner
            if(card.getRank().equals(cardToDelete.getRank()) && card.getSuit().equals(cardToDelete.getSuit())){
                this.activeCards.remove(card);
            }
        }
    }

    /* Draw new cards, used at beginning of new round */
    public void drawCards(ArrayList<Card> cards){
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


    public List<Card> getActiveCards() {
        return this.activeCards;
    }

    public void setActiveCards(ArrayList<Card> activeCards) {
        this.activeCards = activeCards;
    }

}
