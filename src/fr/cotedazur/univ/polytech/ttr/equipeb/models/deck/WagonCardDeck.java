package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.*;

/**
 * Class representing the deck of wagon cards (hidden stack of cards)
 */
public class WagonCardDeck {
    private final Deque<WagonCard> stack;
    private final List<WagonCard> shownCards = new ArrayList<>();
    private final Deque<WagonCard> discardPile = new ArrayDeque<>();


    public WagonCardDeck(List<WagonCard> cards) {
        this.stack = new ArrayDeque<>(cards);
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

    public boolean addCardToDiscardPile(List<WagonCard> cards) {
        return discardPile.addAll(cards);
    }

    public boolean fillDeck() {
        if (stack.isEmpty()) {
            stack.addAll(discardPile);
            discardPile.clear();
            return true;
        }
        return false;
    }

    /**
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean shuffle() {
        List<WagonCard> cards = new ArrayList<>(stack);
        Collections.shuffle(cards);
        stack.clear();
        stack.addAll(cards);
        return true;
    }

    public boolean removeCardFromShownCards(WagonCard card) {
        return shownCards.remove(card);
    }

    public boolean addCardToShownCards(WagonCard card) {
        if (shownCards.size() >= 5) {
            return false;
        }
        shownCards.add(card);
        return true;
    }

    public List<WagonCard> shownCards() {
        return new ArrayList<>(shownCards);
    }

    public boolean replaceShownCards(List<WagonCard> cards) {
        shownCards.clear();
        return shownCards.addAll(cards);
    }
}