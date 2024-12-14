package fr.cotedazur.univ.polytech.ttr.equipeb.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.NonControllableRoute;

import java.util.List;

/**
 * Interface for the player to the game model
 */
public interface IPlayerGameModel {
    /**
     * @return the list of routes for the game with a type encapsulating the route disallowing the player to modify it (set the owner)
     */
    List<NonControllableRoute> getNonControllableRoutes();
}
