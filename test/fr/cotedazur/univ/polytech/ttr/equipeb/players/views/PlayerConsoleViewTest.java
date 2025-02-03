package fr.cotedazur.univ.polytech.ttr.equipeb.players.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.*;

class PlayerConsoleViewTest {
    private PlayerConsoleView playerConsoleView;
    private Logger logger;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        playerConsoleView = new PlayerConsoleView(PlayerIdentification.GREEN);
        logger = mock(Logger.class);
        Field loggerField = PlayerConsoleView.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(playerConsoleView, logger);
    }

    @Test
    void testDisplayClaimedRoute() {
        Route route = mock(Route.class);
        playerConsoleView.displayClaimedRoute(route);
        verify(logger, times(1)).debug("Claimed route: {}", route);
    }

    @Test
    void testDisplayNewScore() {
        playerConsoleView.displayNewScore(0);
        verify(logger, times(1)).info("New score: {}", 0);
    }

    @Test
    void testDisplayClaimedStation() {
        City city = mock(City.class);
        playerConsoleView.displayClaimedStation(city, List.of(), 0);
        verify(logger, times(1)).debug("Claimed station: {} with wagon cards: {}. Stations left: {}", city, List.of(), 0);
    }
}
