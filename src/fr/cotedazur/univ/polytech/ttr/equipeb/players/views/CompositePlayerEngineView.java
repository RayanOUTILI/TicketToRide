package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;

import java.util.ArrayList;
import java.util.List;

public class CompositePlayerEngineView implements IPlayerEngineViewable{

    List<IPlayerEngineViewable> views;

    public CompositePlayerEngineView(){
        this.views = new ArrayList<>();
    }

    public void addView(IPlayerEngineViewable view){
        this.views.add(view);
    }

    @Override
    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        views.forEach(v -> v.displayActionRefused(action, reason));
    }

    @Override
    public void displayActionSkipped(Action action, ReasonActionRefused reason) {
        views.forEach(v -> v.displayActionSkipped(action, reason));
    }

    @Override
    public void displayActionCompleted(Action action) {
        views.forEach(v -> v.displayActionCompleted(action));
    }

    @Override
    public void displayActionStop() {
        views.forEach(IPlayerEngineViewable::displayActionStop);
    }
}
