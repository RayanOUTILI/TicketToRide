package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;

public class GameSimulator {
    private final GameEngine gameEngine;

    public GameSimulator(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public int simulateGame(int nbOfParties) {
        for (int i = 0; i < nbOfParties; i++) {
            gameEngine.initGame();
            gameEngine.initPlayers();
            gameEngine.startGame();
            gameEngine.reset();
        }

        return nbOfParties;
    }
}
