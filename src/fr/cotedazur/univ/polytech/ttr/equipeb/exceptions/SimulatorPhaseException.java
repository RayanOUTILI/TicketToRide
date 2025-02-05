package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

public class SimulatorPhaseException extends RuntimeException {
    public SimulatorPhaseException(SimulatorPhaseError error, int turn) {
        super(error + ": " + error.getMessage(turn));
    }
}
