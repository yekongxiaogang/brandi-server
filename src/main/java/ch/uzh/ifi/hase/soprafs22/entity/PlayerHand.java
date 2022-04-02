package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.ArrayList;

public class PlayerHand {
    private ArrayList<Card> activeCards = new ArrayList<>();
    
    public PlayerHand(ArrayList<Card> cards){
        this.activeCards = cards;
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
    public void drawCards(Deck deck, int amount){
        for(int i = 0; i < amount; i++){
            this.activeCards.add(deck.drawCard());
        }
    }

}
