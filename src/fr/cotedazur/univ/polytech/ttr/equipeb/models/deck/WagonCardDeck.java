package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.*;

/**
 * Class representing the deck of wagon cards (hidden stack of cards)
 */
public class WagonCardDeck {
    private final Deque<WagonCard> stack;


    public WagonCardDeck(List<WagonCard> cards) {
        this.stack = new ArrayDeque<>(cards);
    }

    public WagonCardDeck() {
        this(new ArrayList<>());
    }

    /**
     * @param newCards, the new cards to add
     */
    public void regenerateDeck(List<WagonCard> newCards) {
        this.stack.clear();
        this.stack.addAll(newCards);
    }

    /**
     * @return the first card of the deck and remove it from the deck
     */
    public WagonCard drawCard() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("The deck is empty!");
        }
        return stack.removeFirst();
    }

    /**
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}