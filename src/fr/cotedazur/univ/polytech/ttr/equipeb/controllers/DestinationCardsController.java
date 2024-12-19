package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;

public class DestinationCardsController extends Controller {
    private static final int STARTING_DESTINATION_CARDS = 3;
    private static final int MAX_DESTINATION_CARDS = 3;
    private final IDestinationCardsControllerGameModel gameModel;

    public DestinationCardsController(IDestinationCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean init(Player player) {
        if(gameModel.isDestinationCardDeckEmpty()) return false;

        boolean shuffled = gameModel.shuffleDestinationCardDeck();

        if (!shuffled) return false;

        List<DestinationCard> cards = gameModel.drawDestinationCards(STARTING_DESTINATION_CARDS);

        if(cards == null || cards.isEmpty() || cards.size() != STARTING_DESTINATION_CARDS) return false;

        player.receivedDestinationCards(cards);

        return true;
    }

    @Override
    public boolean doAction(Player player) {
        if(gameModel.isDestinationCardDeckEmpty()) return false;

        List<DestinationCard> cards = gameModel.drawDestinationCards(MAX_DESTINATION_CARDS);
        List<DestinationCard> chosenCards = player.askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            gameModel.returnDestinationCardsToTheBottom(cards);
            return false;
        }

        player.receivedDestinationCards(chosenCards);

        return true;
    }
}
