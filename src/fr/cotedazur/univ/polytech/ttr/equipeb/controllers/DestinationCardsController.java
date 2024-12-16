package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerController;

import java.util.List;

public class DestinationCardsController {
    private IDestinationCardsControllerGameModel gameModel;

    public DestinationCardsController(IDestinationCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public boolean canPlayerPickDestinationCards() {
        return gameModel.getDestinationCardDeck().isEmpty();
    }

    public boolean pickDestinationCards(PlayerController player) {
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
