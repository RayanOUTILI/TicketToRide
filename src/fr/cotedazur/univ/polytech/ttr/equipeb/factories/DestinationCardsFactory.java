package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.ArrayList;
import java.util.List;

public class DestinationCardsFactory {
    public List<DestinationCard> getDestinationCards() {
        ArrayList<DestinationCard> destinationCards = new ArrayList<>();

        destinationCards.add(new DestinationCard("Budapest", "Sofia", 5));
        destinationCards.add(new DestinationCard("Sofia", "Smyrna", 5));
        destinationCards.add(new DestinationCard("Warszawa", "Smolensk", 7));
        destinationCards.add(new DestinationCard("London", "Berlin", 7));
        destinationCards.add(new DestinationCard("Brest", "Marseille", 7));

        return destinationCards;
    }
}
