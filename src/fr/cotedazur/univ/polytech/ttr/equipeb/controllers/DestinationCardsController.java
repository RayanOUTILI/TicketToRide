package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;
import java.util.Optional;

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

        List<ShortDestinationCard> cards = gameModel.drawDestinationCards(STARTING_DESTINATION_CARDS);

        if(cards == null || cards.isEmpty() || cards.size() != STARTING_DESTINATION_CARDS) return false;

        player.receivedDestinationCards(cards);

        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        if(gameModel.isDestinationCardDeckEmpty()) return Optional.of(ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);

        List<ShortDestinationCard> cards = gameModel.drawDestinationCards(MAX_DESTINATION_CARDS);
        List<ShortDestinationCard> chosenCards = player.askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            gameModel.returnDestinationCardsToTheBottom(cards);
            return Optional.of(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY);
        }

        player.receivedDestinationCards(chosenCards);

        return Optional.empty();
    }
}
