package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;

import java.util.ArrayList;
import java.util.List;

public class WagonCardsFactory {
    public List<WagonCard> getWagonCards() {
        List<WagonCard> wagonCards = new ArrayList<>();

        // 12 cards of each color
        addWagonCardsByColor(wagonCards, Color.PINK, 12);
        addWagonCardsByColor(wagonCards, Color.WHITE, 12);
        addWagonCardsByColor(wagonCards, Color.BLUE, 12);
        addWagonCardsByColor(wagonCards, Color.YELLOW, 12);
        addWagonCardsByColor(wagonCards, Color.ORANGE, 12);
        addWagonCardsByColor(wagonCards, Color.BLACK, 12);
        addWagonCardsByColor(wagonCards, Color.RED, 12);
        addWagonCardsByColor(wagonCards, Color.GREEN, 12);

        // 14 Locomotive cards
        addWagonCardsByColor(wagonCards, Color.ANY, 14);

        return wagonCards;
    }

    /**
     * Adds a specific number of WagonCards of a given color to the list.
     *
     * @param wagonCards The list to which the cards will be added.
     * @param color The color of the WagonCards.
     * @param number The number of cards to add.
     */
    private void addWagonCardsByColor(List<WagonCard> wagonCards, Color color, int number) {
        for (int i = 0; i < number; i++) {
            wagonCards.add(new WagonCard(color));
        }
    }
}
