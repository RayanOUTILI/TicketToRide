package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class ScoreConsoleViewTest {
    private ScoreConsoleView scoreConsoleView;
    private Logger logger;
    private PlayerIdentification playerIdentification = PlayerIdentification.GREEN;

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        logger = mock(Logger.class);
        scoreConsoleView = new ScoreConsoleView();

        Field loggerField = ScoreConsoleView.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(scoreConsoleView, logger);
    }

    @Test
    void testDisplayCompletedDestination() {
        DestinationCard destinationCard = mock(DestinationCard.class);

        assertDoesNotThrow(() -> scoreConsoleView.displayCompletedDestination(playerIdentification, destinationCard));
        verify(logger, times(1)).info("Player {} has a destination between {} and {} ({} points)", playerIdentification, null, null, 0);
        verify(destinationCard, times(1)).getStartCity();
        verify(destinationCard, times(1)).getEndCity();
        verify(destinationCard, times(1)).getPoints();
    }

    @Test
    void testDisplayFailedDestination() {
        DestinationCard destinationCard = mock(DestinationCard.class);

        assertDoesNotThrow(() -> scoreConsoleView.displayFailedDestination(playerIdentification, destinationCard));
        verify(logger, times(1)).info("Player {} failed a destination between {} and {} ({} points)", playerIdentification, null, null, 0);
        verify(destinationCard, times(1)).getStartCity();
        verify(destinationCard, times(1)).getEndCity();
        verify(destinationCard, times(1)).getPoints();
    }

    @Test
    void testDisplayPlayerHasLongestPath() {
        int length = 5;

        assertDoesNotThrow(() -> scoreConsoleView.displayPlayerHasLongestPath(playerIdentification, length));
        verify(logger, times(1)).info("Player {} has the longest path ({} routes)", playerIdentification, length);
    }

    @Test
    void testDisplayRemainingStationsScore() {
        int score = 10;

        assertDoesNotThrow(() -> scoreConsoleView.displayRemainingStationsScore(playerIdentification, score));
        verify(logger, times(1)).info("Player {} has {} points from remaining stations", playerIdentification, score);
    }

}