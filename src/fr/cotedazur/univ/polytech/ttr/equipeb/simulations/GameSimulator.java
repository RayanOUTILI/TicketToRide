package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseError;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseException;

public class GameSimulator {
    private final GameEngine gameEngine;

    public GameSimulator(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public int simulateGame(int nbOfParties) {
        for (int i = 0; i < nbOfParties; i++) {
            if (!gameEngine.initGame()) throw new SimulatorPhaseException(SimulatorPhaseError.INIT_GAME_FAILED, i);
            if (!gameEngine.initPlayers()) throw new SimulatorPhaseException(SimulatorPhaseError.INIT_PLAYERS_FAILED, i);
            if (gameEngine.startGame() == 1) throw new SimulatorPhaseException(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, i);
            if (!gameEngine.reset()) throw new SimulatorPhaseException(SimulatorPhaseError.RESET_FAILED, i);
        }
        return nbOfParties;
    }
}
