package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.ArrayList;
import java.util.List;

public class WagonCardsFactory {
    public List<WagonCard> getWagonCards() {
        int numberOfCards = 5;
        List<WagonCard> wagonCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            wagonCards.add(new WagonCard());
        }
        return wagonCards;
    }

    public List<WagonCard> getOneWagonCard() {
        return List.of(new WagonCard());
    }
}
