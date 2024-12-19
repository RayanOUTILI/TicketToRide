package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.*;

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
        List<DestinationCard> cards = new ArrayList<>();

        for (int i = 0; i < maximumDraw && !stack.isEmpty(); i++) {
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

    public boolean shuffle() {
        List<DestinationCard> list = new ArrayList<>(stack);
        Collections.shuffle(list);
        stack.clear();
        stack.addAll(list);
        return true;
    }
}
