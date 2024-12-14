package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

public class VictoryController {
    private final GameModel gameModel;

    public VictoryController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public Victory endGame() {
        if(gameModel.getRoutes().stream().allMatch(Route::isClaimed)) return new Victory(PlayerIdentification.DEFAULT, EndGameReasons.ALL_ROUTES_CLAIMED);
        if(gameModel.getWagonCardDeck().isEmpty()) return new Victory(PlayerIdentification.DEFAULT, EndGameReasons.EMPTY_WAGON_CARDS_DECK);
        return null;
    }
}
