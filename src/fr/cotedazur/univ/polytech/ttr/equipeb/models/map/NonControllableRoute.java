package fr.cotedazur.univ.polytech.ttr.equipeb.models.map;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

/**
 * Represents a route that can be claimed by a player
 * This interface disallows the player to modify the route (set the owner)
 */
public interface NonControllableRoute {

    /**
     * @return the id of the route
     */
    int getId();

    /**
     * @return the first city connected by the route
     */
    City getFirstCity();

    /**
     * @return the second city connected by the route
     */
    City getSecondCity();


    /**
     * @return the length of the route
     */
    int getLength();

    /**
     * @return true if the route is claimed by a player
     */
    boolean isClaimed();


    /**
     * @return the player who claimed the route
     */
    PlayerIdentification getClaimerPlayer();
}
