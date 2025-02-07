package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Player console view using SLF4J
 */
public class PlayerConsoleView extends IPlayerViewable implements IPlayerEngineViewable {
    private final Logger logger = LoggerFactory.getLogger(PlayerConsoleView.class);

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        logger.debug("Player {}: Received wagon cards: {}", super.playerIdentification, wagonCards);
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        logger.debug("Player {}: Received wagon cards: {}", super.playerIdentification, wagonCards);
    }

    @Override
    public void displayClaimedRoute(RouteReadOnly route) {
        logger.debug("Player {}: Claimed route: {}", super.playerIdentification, route);
    }

    @Override
    public void displayReceivedDestinationCards(List<DestinationCard> destinationCards) {
        logger.debug("Player {}: Received destination cards: {}", super.playerIdentification, destinationCards);
    }

    @Override
    public void displayNewScore(int score) {
        logger.info("Player {}: New score: {}", super.playerIdentification, score);
    }

    @Override
    public void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft) {
        logger.debug("Player {}: Claimed station: {} with wagon cards: {}. Stations left: {}", super.playerIdentification, city, wagonCards, stationsLeft);
    }

    @Override
    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        logger.warn("Player {}: Action refused: {} -> {}", super.playerIdentification, action, reason);
    }

    @Override
    public void displayActionSkipped(Action action, ReasonActionRefused reason) {
        logger.warn("Player {}: Action skipped: {} -> {}", super.playerIdentification, action, reason);
    }

    @Override
    public void displayActionCompleted(Action action) {
        logger.info("Player {}: Action completed: {}", super.playerIdentification, action);
    }

    @Override
    public void displayActionStop() {
        logger.info("Player {}: Action Ask Stop Game", super.playerIdentification);
    }
}
