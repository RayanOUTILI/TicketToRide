package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the player to the game model
 */
public interface IPlayerGameModel {
    /**
     * @return the list of routes for the game with a type encapsulating the route disallowing the player to modify it (set the owner)
     */
    List<RouteReadOnly> getNonControllableRoutes();

    /**
     * @return the list of available routes for the game with a type encapsulating the route disallowing the player to modify it (set the owner)
     */
    List<RouteReadOnly> getNonControllableAvailableRoutes();

    /**
     * @return the list of available routes with maxLength for the game with a type encapsulating the route disallowing the player to modify it (set the owner)
     */
    List<RouteReadOnly> getNonControllableAvailableRoutes(int maxLength);

    /**
     * @return the list of available cities for the game with a type encapsulating the city disallowing the player to modify it (set the owner)
     */
    List<CityReadOnly> getNonControllableAvailableCities();

    /**
     * @return if the destination card deck is empty
     */
    boolean isDestinationCardDeckEmpty();
}

