package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.Card;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public void pickCard() {
        this.hand.add(new WagonCard());
    }

    public void showHand() {
        if (hand.isEmpty()) {
            System.out.println("The hand is empty.");
        } else {
            System.out.println("Player's hand :");
            for (Card card : hand) {
                System.out.println(card.toString());
            }
        }
    }

    public List<Card> getHand() {
        return hand;
    }
}
