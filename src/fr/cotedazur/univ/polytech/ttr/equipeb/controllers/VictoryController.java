package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IVictoryControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public class VictoryController {
    private final IVictoryControllerGameModel gameModel;

    public VictoryController(IVictoryControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public Victory endGame() {
        if(gameModel.isAllRoutesClaimed()) return new Victory(PlayerIdentification.DEFAULT, EndGameReasons.ALL_ROUTES_CLAIMED);
        if(gameModel.isWagonCardDeckEmpty()) return new Victory(PlayerIdentification.DEFAULT, EndGameReasons.EMPTY_WAGON_CARDS_DECK);
        return null;
    }
}
