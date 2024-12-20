package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;

public interface IPlayerEngineViewable {
    void displayActionRefused(Action action);
    void displayActionCompleted(Action action);
}
