package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public class GameConsoleView implements IGameViewable {
    @Override
    public void displayEndGameReason(EndGameReasons reason) {
        System.out.println("Game ended: " + reason);
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        System.out.println("Player " + playerId + " won with a score of " + score);
    }

}
