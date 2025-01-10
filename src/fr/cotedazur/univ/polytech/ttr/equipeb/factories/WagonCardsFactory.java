package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.CardColor;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.ArrayList;
import java.util.List;

public class WagonCardsFactory {
    public List<WagonCard> getWagonCards() {
        List<WagonCard> wagonCards = new ArrayList<>();

        // 12 cards of each color
        addWagonCardsByColor(wagonCards, CardColor.VIOLET, 12);
        addWagonCardsByColor(wagonCards, CardColor.WHITE, 12);
        addWagonCardsByColor(wagonCards, CardColor.BLUE, 12);
        addWagonCardsByColor(wagonCards, CardColor.YELLOW, 12);
        addWagonCardsByColor(wagonCards, CardColor.ORANGE, 12);
        addWagonCardsByColor(wagonCards, CardColor.BLACK, 12);
        addWagonCardsByColor(wagonCards, CardColor.RED, 12);
        addWagonCardsByColor(wagonCards, CardColor.GREEN, 12);

        // 14 Locomotive cards
        addWagonCardsByColor(wagonCards, CardColor.MULTICOLOR, 14);

        return wagonCards;
    }

    /**
     * Adds a specific number of WagonCards of a given color to the list.
     *
     * @param wagonCards The list to which the cards will be added.
     * @param color The color of the WagonCards.
     * @param number The number of cards to add.
     */
    private void addWagonCardsByColor(List<WagonCard> wagonCards, CardColor color, int number) {
        for (int i = 0; i < number; i++) {
            wagonCards.add(new WagonCard(color));
        }
    }
}
