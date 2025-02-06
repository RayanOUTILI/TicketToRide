package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

public class SimulatorPhaseException extends RuntimeException {
    private SimulatorPhaseError error;
    private int turn;
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
