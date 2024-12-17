package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;

public class DestinationCardsController extends Controller {
    private final IDestinationCardsControllerGameModel gameModel;

    public DestinationCardsController(IDestinationCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean doAction(Player player) {
        if(gameModel.isDestinationCardDeckEmpty()) return false;

        List<DestinationCard> cards = gameModel.drawDestinationCards(3);
        List<DestinationCard> chosenCards = player.askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            gameModel.returnDestinationCardsToTheBottom(cards);
            return false;
        }

        player.receivedDestinationCards(chosenCards);

        return true;
    }
}
