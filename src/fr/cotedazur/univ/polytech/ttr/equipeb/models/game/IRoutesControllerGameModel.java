package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.List;

public interface IRoutesControllerGameModel {
    boolean setAllRoutesNotClaimed();
    Route getRoute(int id);

    /**
     * Get the double route of the given route
     * @param id the id of the route
     * @return the double route of the given route or null
     */
    Route getDoubleRouteOf(int id);

    boolean deleteRoute(int id);

    int getNbOfPlayers();
}
