package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.Card;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a deck of cards.
 */
public class CardDeck {
    private LinkedList<Card> deck;

    /**
     * Constructs a CardDeck with a given list of cards.
     *
     * @param cards the list of cards to initialize the deck with
     */
    public CardDeck(List<Card> cards) {
        this.deck = new LinkedList<>(cards);
    }

    /**
     * Constructs an empty CardDeck.
     */
    public CardDeck() {
        this.deck = new LinkedList<>();
    }

    /**
     * Regenerates the deck with a new list of cards.
     *
     * @param newCards the new list of cards to replace the current deck
     */
    public void regenerateDeck(List<Card> newCards) {
        this.deck.clear();
        this.deck.addAll(newCards);
    }

    /**
     * Draws the card at the top of the deck.
     *
     * @return the card at the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("The deck is empty!");
        }
        return deck.removeFirst();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Gets the current size of the deck.
     *
     * @return the number of cards in the deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * Adds a card to the deck.
     *
     * @param card the card to add to the deck
     */
    public void addCard(Card card) {
        deck.add(card);
    }

    /**
     * Gets the card at the specified index in the deck.
     *
     * @param index the index of the card to retrieve
     * @return the card at the specified index
     */
    public Card getCard(int index) {
        return deck.get(index);
    }
}