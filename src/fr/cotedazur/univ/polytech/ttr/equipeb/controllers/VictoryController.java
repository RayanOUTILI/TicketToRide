package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IVictoryControllerGameModel;

public class VictoryController {
    private final IVictoryControllerGameModel gameModel;

    public VictoryController(IVictoryControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public EndGameReasons endGame() {
        if(gameModel.isAllRoutesClaimed()) return EndGameReasons.ALL_ROUTES_CLAIMED;
        if(gameModel.isWagonCardDeckEmpty()) return EndGameReasons.EMPTY_WAGON_CARDS_DECK;
        return null;
    }

    public void endTurn() {
        if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();
    }
}
