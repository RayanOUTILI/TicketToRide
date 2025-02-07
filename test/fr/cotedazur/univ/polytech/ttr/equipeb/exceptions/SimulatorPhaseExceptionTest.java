package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimulatorPhaseExceptionTest {

    @Test
    void constructorInitializesFieldsCorrectly() {
        SimulatorPhaseException exception = new SimulatorPhaseException(SimulatorPhaseError.INIT_GAME_FAILED, 5);

        assertEquals(SimulatorPhaseError.INIT_GAME_FAILED, exception.getError());
        assertEquals(5, exception.getTurn());
        assertEquals("INIT_GAME_FAILED: Failed to initialize the game at turn 5", exception.getMessage());
    }

    @Test
    void getErrorReturnsCorrectError() {
        SimulatorPhaseException exception = new SimulatorPhaseException(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, 3);

        assertEquals(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, exception.getError());
    }

    @Test
    void getTurnReturnsCorrectTurn() {
        SimulatorPhaseException exception = new SimulatorPhaseException(SimulatorPhaseError.INIT_GAME_FAILED, 7);

        assertEquals(7, exception.getTurn());
    }

    @Test
    void exceptionMessageIsFormattedCorrectly() {
        SimulatorPhaseException exception = new SimulatorPhaseException(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, 2);

        assertEquals("GAME_SHOULD_NOT_END_AFTER_FIRST_TURN: Game should not end after the first turn at turn 2", exception.getMessage());
    }
}
