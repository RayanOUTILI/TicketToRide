package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.*;

public class DestinationCardDeck<T extends DestinationCard>{
    private final Deque<T> stack;

    public DestinationCardDeck(List<T> cards) {
        this.stack = new ArrayDeque<>();
        this.stack.addAll(cards);
    }

    public List<T> drawCard(int maximumDraw) {
        List<T> cards = new ArrayList<>();

        for (int i = 0; i < maximumDraw && !stack.isEmpty(); i++) cards.add(stack.removeFirst());

        return cards;
    }

    public void addCardsAtBottom(List<T> cards) {
        cards.forEach(stack::addLast);
    }

    /**
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean shuffle() {
        List<T> list = new ArrayList<>(stack);
        Collections.shuffle(list);
        stack.clear();
        stack.addAll(list);
        return true;
    }
}
