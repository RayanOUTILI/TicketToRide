package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;

public interface IPlayerEngineViewable {
    void displayActionRefused(Action action, ReasonActionRefused reason);
    void displayActionCompleted(Action action);
    void displayActionStop();
}
