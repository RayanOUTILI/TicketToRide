package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseError;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameExecutorTest {
    private GameEngine mockGameEngine;
    private GameModel mockGameModel;
    private GameExecutor gameExecutor;

    @BeforeEach
    void setup() {
        mockGameEngine = mock(GameEngine.class);
        mockGameModel = mock(GameModel.class);
        gameExecutor = new GameExecutor(mockGameEngine, mockGameModel);
    }

    @Test
    void testGetGameModel() {
        assertEquals(mockGameModel, gameExecutor.getGameModel());
    }

    @Test
    void testExecuteSuccess() {
        when(mockGameEngine.initGame()).thenReturn(true);
        when(mockGameEngine.initPlayers()).thenReturn(true);
        when(mockGameEngine.startGame()).thenReturn(0);
        when(mockGameEngine.reset()).thenReturn(true);

        int result = gameExecutor.execute(1);
        assertEquals(1, result);

        verify(mockGameEngine, times(1)).initGame();
        verify(mockGameEngine, times(1)).initPlayers();
        verify(mockGameEngine, times(1)).startGame();
        verify(mockGameEngine, times(1)).reset();
    }

    @Test
    void testInitGameFailure() {
        when(mockGameEngine.initGame()).thenReturn(false);

        SimulatorPhaseException exception = assertThrows(SimulatorPhaseException.class, () -> gameExecutor.execute(1));
        assertEquals(SimulatorPhaseError.INIT_GAME_FAILED, exception.getError());
        assertEquals(0, exception.getTurn());

        verify(mockGameEngine, times(1)).initGame();
        verify(mockGameEngine, never()).initPlayers();
        verify(mockGameEngine, never()).startGame();
        verify(mockGameEngine, never()).reset();
    }

    @Test
    void testInitPlayersFailure() {
        when(mockGameEngine.initGame()).thenReturn(true);
        when(mockGameEngine.initPlayers()).thenReturn(false);

        SimulatorPhaseException exception = assertThrows(SimulatorPhaseException.class, () -> gameExecutor.execute(1));
        assertEquals(SimulatorPhaseError.INIT_PLAYERS_FAILED, exception.getError());
        assertEquals(0, exception.getTurn());

        verify(mockGameEngine, times(1)).initGame();
        verify(mockGameEngine, times(1)).initPlayers();
        verify(mockGameEngine, never()).startGame();
        verify(mockGameEngine, never()).reset();
    }

    @Test
    void testStartGameFailure() {
        when(mockGameEngine.initGame()).thenReturn(true);
        when(mockGameEngine.initPlayers()).thenReturn(true);
        when(mockGameEngine.startGame()).thenReturn(1);

        SimulatorPhaseException exception = assertThrows(SimulatorPhaseException.class, () -> gameExecutor.execute(1));
        assertEquals(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, exception.getError());
        assertEquals(0, exception.getTurn());

        verify(mockGameEngine, times(1)).initGame();
        verify(mockGameEngine, times(1)).initPlayers();
        verify(mockGameEngine, times(1)).startGame();
        verify(mockGameEngine, never()).reset();
    }

    @Test
    void testResetFailure() {
        when(mockGameEngine.initGame()).thenReturn(true);
        when(mockGameEngine.initPlayers()).thenReturn(true);
        when(mockGameEngine.startGame()).thenReturn(0);
        when(mockGameEngine.reset()).thenReturn(false);

        SimulatorPhaseException exception = assertThrows(SimulatorPhaseException.class, () -> gameExecutor.execute(1));
        assertEquals(SimulatorPhaseError.RESET_FAILED, exception.getError());
        assertEquals(0, exception.getTurn());

        verify(mockGameEngine, times(1)).initGame();
        verify(mockGameEngine, times(1)).initPlayers();
        verify(mockGameEngine, times(1)).startGame();
        verify(mockGameEngine, times(1)).reset();
    }
}