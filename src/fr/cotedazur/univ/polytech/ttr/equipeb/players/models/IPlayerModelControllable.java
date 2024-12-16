package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.NonControllableRoute;

import java.util.List;

/**
 * Interface to control the player model outside the player engine (GameEngine)
 */
public interface IPlayerModelControllable {
    PlayerIdentification getIdentification();
    void receivedWagonCard(WagonCard wagonCard);
    int removeWagonCards(List<WagonCard> wagonCards);
    void notifyClaimedRoute(NonControllableRoute route);
    void receivedDestinationCards(List<DestinationCard> destinationCards);
}
