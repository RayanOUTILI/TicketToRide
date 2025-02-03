package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameConsoleView implements IGameViewable {
    private final Logger logger = LoggerFactory.getLogger(GameConsoleView.class);

    @Override
    public void displayEndGameReason(PlayerIdentification playerId, int nbOfWagons) {
        logger.info("Player {} has run out of wagons ({} wagons left)", playerId, nbOfWagons);
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        logger.info("Player {} has won with a score of {}", playerId, score);
    }
}
