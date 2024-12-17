package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
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
        if(gameModel.getDestinationCardDeck().isEmpty()) return false;
        DestinationCardDeck deck = gameModel.getDestinationCardDeck();

        List<DestinationCard> cards = deck.drawCard(3);
        List<DestinationCard> chosenCards = player.actionsController().askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            deck.addCardsAtBottom(cards);
            return false;
        }

        player.modelController().receivedDestinationCards(chosenCards);

        return true;
    }
}
