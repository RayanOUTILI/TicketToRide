package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameConsoleView implements IGameViewable {
    private final Logger logger = Logger.getLogger(GameConsoleView.class.getSimpleName());

    public GameConsoleView() {
        logger.setLevel(Level.INFO);
    }

    @Override
    public void displayEndGameReason(PlayerIdentification playerId, int nbOfWagons) {
        logger.log(Level.INFO, "Player " + playerId + " has run out of wagons");
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        logger.log(Level.INFO, "Player " + playerId + " won with a score of " + score);
    }

}
