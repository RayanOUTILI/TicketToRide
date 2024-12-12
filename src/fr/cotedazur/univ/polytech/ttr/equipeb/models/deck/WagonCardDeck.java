package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.LinkedList;
import java.util.List;

public class WagonCardDeck {
    private LinkedList<WagonCard> deck;

    // Constructor
    public WagonCardDeck(List<WagonCard> cards) {
        this.deck = new LinkedList<>(cards);
    }

    public WagonCardDeck() {
        this.deck = new LinkedList<>();
    }

    // Regenerate the deck with a new list of cards
    public void regenerateDeck(List<WagonCard> newCards) {
        this.deck.clear();
        this.deck.addAll(newCards);
    }

    // Draw the card at the top of the deck
    public WagonCard drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty!");
        }
        return deck.removeFirst();
    }

    // Check if the deck is empty
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    // Get the current size of the deck
    public int size() {
        return deck.size();
    }

    public void addCard(WagonCard card) {
        deck.add(card);
    }

    public WagonCard getWagonCard(int index) {
        return deck.get(index);
    }
}