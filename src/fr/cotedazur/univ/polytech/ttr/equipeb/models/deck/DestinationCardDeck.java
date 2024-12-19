package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;

import java.util.*;

public class DestinationCardDeck {
    private final Deque<ShortDestinationCard> stack;

    public DestinationCardDeck(List<ShortDestinationCard> cards) {
        this.stack = new ArrayDeque<>(cards);
    }

    public List<ShortDestinationCard> drawCard(int maximumDraw) {
        List<ShortDestinationCard> cards = new ArrayList<>();

        for (int i = 0; i < maximumDraw && !stack.isEmpty(); i++) cards.add(stack.removeFirst());

        return cards;
    }

    public void addCardsAtBottom(List<ShortDestinationCard> cards) {
        cards.forEach(stack::addLast);
    }

    /**
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean shuffle() {
        List<ShortDestinationCard> list = new ArrayList<>(stack);
        Collections.shuffle(list);
        stack.clear();
        stack.addAll(list);
        return true;
    }
}
