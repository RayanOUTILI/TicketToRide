package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;

/**
 * THIS INTERFACE IS USED BY THE GAME ENGINE TO CONTROL THE PLAYER ACTIONS
 * AND BY THE CONTROLLERS TO CONTROL THE PLAYER MODEL
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

    List<DestinationCard> getDestinationCardsHand();

    void defineStartingStationsNumber(int size);
    int getStationsLeft();
    void decrementStationsLeft();
    void notifyClaimedStation(CityReadOnly city, List<WagonCard> wagonCards);

    boolean setNumberOfWagons(int startingWagonCards);

    int getNumberOfWagons();

    void removeWagons(int numberOfWagons);
}
