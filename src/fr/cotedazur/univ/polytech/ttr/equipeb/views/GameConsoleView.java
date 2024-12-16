package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;

public class GameConsoleView implements IGameViewable {
    @Override
    public void displayEndGameReason(EndGameReasons reason) {
        System.out.println("Game ended: " + reason);
    }
}
