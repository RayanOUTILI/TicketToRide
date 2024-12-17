package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

public class WagonCardsController extends Controller {
    private final IWagonCardsControllerGameModel gameModel;

    public WagonCardsController(IWagonCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean doAction(Player player) {
        if (gameModel.isWagonCardDeckEmpty()) return false;

        player.receivedWagonCard(gameModel.drawCardFromWagonCardDeck());

        return true;
    }
}
