package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.ArrayList;
import java.util.List;

public class CompositeGameEngineView implements IGameViewable{

    List<IGameViewable> views;

    public CompositeGameEngineView(){
        this.views = new ArrayList<>();
    }

    public void addView(IGameViewable view){
        this.views.add(view);
    }

    @Override
    public void displayNewGame() {
        views.forEach(IGameViewable::displayNewGame);
    }

    @Override
    public void displayNewTurn(int currentTurn) {
        views.forEach(v -> v.displayNewTurn(currentTurn));
    }

    @Override
    public void displayEndGameReason(PlayerIdentification playerIdentification, int nbOfWagons, int nbTurns) {
        views.forEach(v -> v.displayEndGameReason(playerIdentification, nbOfWagons, nbTurns));
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        views.forEach(v -> v.displayWinner(playerId, score));
    }
}
