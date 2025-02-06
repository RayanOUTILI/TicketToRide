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

    public PlayerConsoleView() {
    }

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        logger.debug("Received wagon cards: {}", (Object) wagonCards);
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        logger.debug("Received wagon cards: {}", wagonCards);
    }

    @Override
    public void displayClaimedRoute(RouteReadOnly route) {
        logger.debug("Claimed route: {}", route);
    }

    @Override
    public void displayReceivedDestinationCards(List<DestinationCard> destinationCards) {
        logger.debug("Received destination cards: {}", destinationCards);
    }

    @Override
    public void displayNewScore(int score) {
        logger.info("New score: {}", score);
    }

    @Override
    public void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft) {
        logger.debug("Claimed station: {} with wagon cards: {}. Stations left: {}", city, wagonCards, stationsLeft);
    }

    @Override
    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        logger.warn("Action refused: {} -> {}", action, reason);
    }

    @Override
    public void displayActionSkipped(Action action, ReasonActionRefused reason) {
        log(Level.WARNING, "Action skipped: " + action + " -> " + reason);
    }

    @Override
    public void displayActionCompleted(Action action) {
        logger.info("Action completed: {}", action);
    }

    @Override
    public void displayActionStop() {
        logger.info("Action Ask Stop Game");
    }
}
