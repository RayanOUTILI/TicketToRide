package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreConsoleView implements IScoreView {
    private final Logger logger = LoggerFactory.getLogger(ScoreConsoleView.class);

    @Override
    public void displayCompletedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        logger.info("Player {} has a destination between {} and {} ({} points)",
                playerId, destinationCard.getStartCity(), destinationCard.getEndCity(), destinationCard.getPoints());
    }

    @Override
    public void displayFailedDestination(PlayerIdentification playerId, DestinationCard destinationCard) {
        logger.info("Player {} failed a destination between {} and {} ({} points)",
                playerId, destinationCard.getStartCity(), destinationCard.getEndCity(), -destinationCard.getPoints());
    }

    @Override
    public void displayPlayerHasLongestPath(PlayerIdentification playerId, int length) {
        logger.info("Player {} has the longest path ({} routes)", playerId, length);
    }

    @Override
    public void displayRemainingStationsScore(PlayerIdentification playerId, int score) {
        logger.info("Player {} has {} points from remaining stations", playerId, score);
    }
}
