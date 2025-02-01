package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class PlayerConsoleViewTest {
    private PlayerConsoleView playerConsoleView;
    private Logger logger;
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        playerConsoleView = new PlayerConsoleView(PlayerIdentification.GREEN);
        logger = mock(Logger.class);
        when(logger.isLoggable(any())).thenReturn(true);
        Field loggerField = PlayerConsoleView.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(playerConsoleView, logger);
    }

    @Test
    void testDisplayReceivedWagonCards() {
        playerConsoleView.displayReceivedWagonCards();
        verify(logger, times(1)).log(Level.FINE, String.format("Player %s: Received wagon cards: ", PlayerIdentification.GREEN));
    }

    @Test
    void testDisplayClaimedRoute() {
        Route route = mock(Route.class);
        playerConsoleView.displayClaimedRoute(route);
        verify(logger, times(1)).log(Level.FINE, String.format("Player %s: Claimed route: %s", PlayerIdentification.GREEN, route));
    }

    @Test
    void testDisplayReceivedDestinationCards() {
        playerConsoleView.displayReceivedDestinationCards(List.of());
        verify(logger, times(1)).log(Level.FINE, String.format("Player %s: Received destination cards: ", PlayerIdentification.GREEN));
    }

    @Test
    void testDisplayNewScore() {
        playerConsoleView.displayNewScore(0);
        verify(logger, times(1)).log(Level.FINE, String.format("Player %s: New score: 0", PlayerIdentification.GREEN));
    }

    @Test
    void testDisplayClaimedStation() {
        City city = mock(City.class);
        playerConsoleView.displayClaimedStation(city, List.of(), 0);
        verify(logger, times(1)).log(Level.FINE, String.format("Player %s: Claimed station: %s with wagon cards: Stations left: 0", PlayerIdentification.GREEN, city));
    }
}