package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DestinationCardDeck {
    private final Deque<DestinationCard> stack;


    public DestinationCardDeck(List<DestinationCard> cards) {
        this.stack = new ArrayDeque<>(cards);
    }

    public DestinationCardDeck() {
        this(new ArrayList<>());
    }

    /**
     * @return the first card of the deck and remove it from the deck
     */
    public DestinationCard drawCard() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("The deck is empty!");
        }
        return stack.removeFirst();
    }

    public List<DestinationCard> drawCard(int maximumDraw) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("The deck is empty!");
        }

        List<DestinationCard> cards = new ArrayList<>();

        for (int i = 0; i < maximumDraw; i++) {
            cards.add(stack.removeFirst());
        }

        return cards;
    }

    public void addCardsAtBottom(List<DestinationCard> cards) {
        cards.forEach(stack::addLast);
    }

    /**
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
