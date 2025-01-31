package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Player console view
 */
public class PlayerConsoleView extends IPlayerViewable implements IPlayerEngineViewable {
    private final Logger logger;

    public PlayerConsoleView(PlayerIdentification playerIdentification) {
        super(playerIdentification);
        logger = Logger.getLogger(String.format("PlayerConsoleView {%s}", playerIdentification));
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.OFF);
        logger.setLevel(Level.OFF);
        logger.addHandler(consoleHandler);
    }

    private void log(Level level, String message) {
        if (logger.isLoggable(level)) {
            logger.log(level, String.format("Player %s: %s", getPlayerIdentification(), message));
        }
    }

    @Override
    public void displayReceivedWagonCards(WagonCard... wagonCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        log(Level.FINE, sb.toString());
    }

    @Override
    public void displayReceivedWagonCards(List<WagonCard> wagonCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        log(Level.FINE, sb.toString());
    }

    @Override
    public void displayClaimedRoute(RouteReadOnly route)  {
        log(Level.FINE, "Claimed route: " + route);
    }

    @Override
    public void displayReceivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        StringBuilder sb = new StringBuilder();
        sb.append("Received destination cards: ");
        for (DestinationCard destinationCard : destinationCards) {
            sb.append(destinationCard).append(" ");
        }
        log(Level.FINE, sb.toString());
    }

    @Override
    public void displayNewScore(int score) {
        log(Level.FINE, "New score: " + score);
    }

    @Override
    public void displayClaimedStation(CityReadOnly city, List<WagonCard> wagonCards, int stationsLeft) {
        StringBuilder sb = new StringBuilder();
        sb.append("Claimed station: ").append(city).append(" with wagon cards: ");
        for (WagonCard wagonCard : wagonCards) {
            sb.append(wagonCard).append(" ");
        }
        sb.append("Stations left: ").append(stationsLeft);
        log(Level.FINE, sb.toString());
    }

    @Override
    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        log(Level.WARNING, "Action refused: " + action + " -> " + reason);
    }

    @Override
    public void displayActionCompleted(Action action) {
        log(Level.INFO,  "Action completed: " + action);
    }
}
