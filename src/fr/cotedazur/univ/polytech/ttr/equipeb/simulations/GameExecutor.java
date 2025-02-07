package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseError;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.SimulatorPhaseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;

public class GameExecutor {
    private final GameEngine gameEngine;
    private GameModel gameModel;

    public GameExecutor(GameEngine gameEngine, GameModel gameModel) {
        this.gameEngine = gameEngine;
        this.gameModel = gameModel;
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public int execute(int nbOfParties) {
        for (int i = 0; i < nbOfParties; i++) {
            if (!gameEngine.initGame()) throw new SimulatorPhaseException(SimulatorPhaseError.INIT_GAME_FAILED, i);
            if (!gameEngine.initPlayers()) throw new SimulatorPhaseException(SimulatorPhaseError.INIT_PLAYERS_FAILED, i);
            if (gameEngine.startGame() == 1) throw new SimulatorPhaseException(SimulatorPhaseError.GAME_SHOULD_NOT_END_AFTER_FIRST_TURN, i);
            if (!gameEngine.reset()) throw new SimulatorPhaseException(SimulatorPhaseError.RESET_FAILED, i);
        }
        return nbOfParties;
    }
}
