package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

public class SimulatorPhaseException extends RuntimeException {
    private final SimulatorPhaseError error;
    private final int turn;
    public SimulatorPhaseException(SimulatorPhaseError error, int turn) {
        super(error + ": " + error.getMessage(turn));
        this.error = error;
        this.turn = turn;
    }

    public SimulatorPhaseError getError() {
        return error;
    }

    public int getTurn() {
        return turn;
    }
}
