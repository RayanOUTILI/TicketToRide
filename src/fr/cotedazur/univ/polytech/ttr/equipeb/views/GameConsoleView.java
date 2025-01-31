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
        if(logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s has run out of wagons", playerId));
        }
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        if(logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s has won with a score of %d", playerId, score));
        }
    }

}
