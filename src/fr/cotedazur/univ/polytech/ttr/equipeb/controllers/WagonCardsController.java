package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

import java.util.List;

public class WagonCardsController {
    private final IWagonCardsControllerGameModel gameModel;

    public WagonCardsController(IWagonCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public int removeWagonCardsToPlayer(IPlayerModelControllable player, List<WagonCard> wagonCards) {
        return player.removeWagonCards(wagonCards);
    }

    protected void pickWagonCard(PlayerController player) {
        player.modelController().receivedWagonCard(gameModel.getWagonCardDeck().drawCard());
    }
}
