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
    private final List<WagonCard> maskedCards = new ArrayList<>();


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
        boolean added = false;
        if (stack.isEmpty()) {
            added = stack.addAll(discardPile);
            discardPile.clear();
        }
        return added && !stack.isEmpty();
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
        return stack.addAll(cards);
    }

    public boolean removeCardFromShownCards(WagonCard card) {
        return shownCards.remove(card);
    }

    public boolean addCardToShownCards(WagonCard card) {
        if (shownCards.size() >= 5) {
            return false;
        }
        return shownCards.add(card);
    }

    public List<WagonCard> shownCards() {
        return new ArrayList<>(shownCards);
    }

    public boolean replaceShownCards(List<WagonCard> cards) {
        maskedCards.addAll(shownCards);
        shownCards.clear();
        return shownCards.addAll(cards);
    }

    public boolean clearDeck() {
        boolean added = true;

        if(!shownCards.isEmpty()) {
            added = stack.addAll(shownCards);
        }
        if(!maskedCards.isEmpty() && added) {
            added = stack.addAll(maskedCards);
        }
        if(!discardPile.isEmpty() && added) {
            added = stack.addAll(discardPile);
        }
        shownCards.clear();
        maskedCards.clear();
        discardPile.clear();
        return added;
    }
}