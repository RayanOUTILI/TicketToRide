package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreConsoleView implements IScoreView{
    private final Logger logger = Logger.getLogger(GameConsoleView.class.getSimpleName());

    public ScoreConsoleView() {
        logger.setLevel(Level.INFO);
    }

    @Override
    public void displayCompletedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s has a destination between %s and %s (%d points)",
                    playerId, destinationCard.getStartCity(), destinationCard.getEndCity(), destinationCard.getPoints()));
        }
    }

    @Override
    public void displayFailedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s failed a destination between %s and %s (%d points)",
                    playerId, destinationCard.getStartCity(), destinationCard.getEndCity(), -destinationCard.getPoints()));
        }
    }

    @Override
    public void displayPlayerHasLongestPath(PlayerIdentification playerId, int length) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s has the longest path (%d routes)", playerId, length));
        }
    }

    @Override
    public void displayRemainingStationsScore(PlayerIdentification playerId, int score) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format("Player %s has %d points from remaining stations", playerId, score));
        }
    }
}
