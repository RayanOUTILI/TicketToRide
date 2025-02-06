package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

public enum SimulatorPhaseError {
    INIT_GAME_FAILED("Failed to initialize the game"),
    INIT_PLAYERS_FAILED("Failed to initialize the players"),
    GAME_SHOULD_NOT_END_AFTER_FIRST_TURN("Game should not end after the first turn"),
    RESET_FAILED("Failed to reset the game");

    private final String message;

    SimulatorPhaseError(String message) {
        this.message = message;
    }

    public String getMessage(int turn) {
        return message + " at turn " + turn;
    }
}
