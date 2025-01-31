package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.List;

public interface IRoutesControllerGameModel {
    boolean setAllRoutesNotClaimed();
    boolean setAllRoutesIDs();
    Route getRoute(int id);

    /**
     * Get the double route of the given route
     * @param id the id of the route
     * @return the double route of the given route or null
     */
    Route getDoubleRouteOf(int id);

    /**
     * Remove the route from the game
     * @param id the ID of the route
     * @return true if the route was removed, false otherwise
     */
    boolean deleteRoute(int id);

    /**
     * Get the number of players in the game
     * @return the number of players in the game
     */
    int getNbOfPlayers();

    /**
     * Discard the given wagon cards to the pile
     * @param wagonCards the wagon cards to discard
     * @return true if the cards were discarded, false otherwise
     */
    boolean discardWagonCards(List<WagonCard> wagonCards);

    List<WagonCard> drawCardsFromWagonCardDeck(int numberOfCards);
}
