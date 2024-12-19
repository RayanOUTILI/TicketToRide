package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.List;

public interface IRoutesControllerGameModel {
    boolean setAllRoutesNotClaimed();
    Route getRoute(int id);
}
