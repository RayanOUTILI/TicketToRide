package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;

/**
 * Interface to control the player model outside the player engine (GameEngine)
 */
public interface IPlayerModelControllable {
    PlayerIdentification getIdentification();
    void receivedWagonCard(WagonCard wagonCard);
    void receivedWagonCards(List<WagonCard> wagonCards);
    List<WagonCard> removeWagonCards(List<WagonCard> wagonCards);

    /**
     * Replace the removed wagon cards to the player in case of failure to claim a route
     */
    void replaceRemovedWagonCards(List<WagonCard> wagonCards);
    void notifyClaimedRoute(RouteReadOnly route);
    void setScore(int score);
    int getScore();
    void receivedDestinationCards(List<ShortDestinationCard> destinationCards);
}
