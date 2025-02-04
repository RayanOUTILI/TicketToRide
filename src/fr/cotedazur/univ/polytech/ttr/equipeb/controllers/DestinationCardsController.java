package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;
import java.util.Optional;

public class DestinationCardsController extends Controller {
    private static final int STARTING_SHORT_DESTINATION_CARDS = 3;
    private static final int STARTING_LONG_DESTINATION_CARDS = 1;
    private static final int MAX_DESTINATION_CARDS = 3;
    private final IDestinationCardsControllerGameModel gameModel;

    public DestinationCardsController(IDestinationCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean initGame() {
        return gameModel.shuffleDestinationCardsDecks();
    }

    @Override
    public boolean initPlayer(Player player) {
        if(gameModel.isShortDestCardDeckEmpty() || gameModel.isLongDestCardDeckEmpty()) return false;

        List<ShortDestinationCard> cards = gameModel.drawDestinationCards(STARTING_SHORT_DESTINATION_CARDS);

        if(cards == null || cards.isEmpty() || cards.size() != STARTING_SHORT_DESTINATION_CARDS) return false;

        player.receivedDestinationCards(cards);

        List<LongDestinationCard> longDestCards = gameModel.drawLongDestinationCards(STARTING_LONG_DESTINATION_CARDS);

        if(longDestCards == null || longDestCards.isEmpty() || longDestCards.size() != STARTING_LONG_DESTINATION_CARDS) return false;

        player.receiveLongDestCards(longDestCards);

        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        if(gameModel.isShortDestCardDeckEmpty()) return Optional.of(ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);

        List<ShortDestinationCard> cards = gameModel.drawDestinationCards(MAX_DESTINATION_CARDS);
        List<ShortDestinationCard> chosenCards = player.askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            gameModel.returnShortDestinationCardsToTheBottom(cards);
            return Optional.of(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY);
        }

        player.receivedDestinationCards(chosenCards);

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        List<ShortDestinationCard> cards = player.getShortDestinationCardsHand();
        gameModel.returnShortDestinationCardsToTheBottom(cards);
        List<LongDestinationCard> longCards = player.getLongDestinationCardsHand();
        gameModel.returnLongDestinationCardsToTheBottom(longCards);
        return player.clearDestinationCards();
    }

    @Override
    public boolean resetGame() {
        return !gameModel.isShortDestCardDeckEmpty() && !gameModel.isLongDestCardDeckEmpty();
    }
}
