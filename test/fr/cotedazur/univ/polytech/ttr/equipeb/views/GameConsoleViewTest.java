package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class GameConsoleViewTest {
    private GameConsoleView gameConsoleView;
    private Logger logger;
    private PlayerIdentification playerIdentification = PlayerIdentification.GREEN;

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        logger = mock(Logger.class);
        gameConsoleView = new GameConsoleView();

        Field loggerField = GameConsoleView.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(gameConsoleView, logger);
    }

    @Test
    void testDisplayNewGame() {
        assertDoesNotThrow(() -> gameConsoleView.displayNewGame());
        verify(logger, times(1)).info("New game");
    }

    @Test
    void testDisplayNewTurn() {
        int currentTurn = 1;

        assertDoesNotThrow(() -> gameConsoleView.displayNewTurn(currentTurn));
        verify(logger, times(1)).info("--------- New turn: {} ----------", currentTurn);
    }

    @Test
    void testDisplayEndGameReason() {
        int nbOfWagons = 0;
        int nbTurns = 10;

        assertDoesNotThrow(() -> gameConsoleView.displayEndGameReason(playerIdentification, nbOfWagons, nbTurns));
        verify(logger, times(1)).info("-------- End of the Game --------");
        verify(logger, times(1)).info("Player {} has run out of wagons ({} wagons left) after {} turns", playerIdentification, nbOfWagons, nbTurns);
        verify(logger, times(1)).info("-------- --------------- --------");
    }

    @Test
    void testDisplayWinner() {
        int score = 100;

        assertDoesNotThrow(() -> gameConsoleView.displayWinner(playerIdentification, score));
        verify(logger, times(1)).info("------------ Winner ------------");
        verify(logger, times(1)).info("Player {} has won with a score of {}", playerIdentification, score);
        verify(logger, times(1)).info("------------ ------ ------------");
    }
}