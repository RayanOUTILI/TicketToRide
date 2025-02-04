package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DestinationCardsController extends Controller {
    private static final int STARTING_SHORT_DESTINATION_CARDS = 3;
    private static final int STARTING_LONG_DESTINATION_CARDS = 1;
    private static final int MAX_DESTINATION_CARDS = 3;
    private static final int DESTINATION_CARD_TYPE_THRESHOLD = 20;
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

        List<DestinationCard> shortDestinationCards = gameModel.drawDestinationCards(STARTING_SHORT_DESTINATION_CARDS);

        if(shortDestinationCards == null
                || shortDestinationCards.isEmpty()
                || shortDestinationCards.size() != STARTING_SHORT_DESTINATION_CARDS)
            return false;



        List<DestinationCard> longDestCards = gameModel.drawLongDestinationCards(STARTING_LONG_DESTINATION_CARDS);

        if(longDestCards == null || longDestCards.isEmpty() || longDestCards.size() != STARTING_LONG_DESTINATION_CARDS) return false;

        List<DestinationCard> drawnCards = new ArrayList<>(shortDestinationCards);
        drawnCards.addAll(longDestCards);

        List<DestinationCard> chosenCards = player.askInitialDestinationCards(drawnCards);

        player.receiveDestinationCards(chosenCards);
        if (drawnCards.removeAll(chosenCards)) {
            player.discardDestinationCard(drawnCards);
        }
        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        if(gameModel.isShortDestCardDeckEmpty()) return Optional.of(ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);

        List<DestinationCard> cards = gameModel.drawDestinationCards(MAX_DESTINATION_CARDS);
        List<DestinationCard> chosenCards = player.askDestinationCards(cards);

        if (chosenCards == null || chosenCards.isEmpty()) {
            gameModel.returnShortDestinationCardsToTheBottom(cards);
            return Optional.of(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY);
        }

        player.receiveDestinationCards(chosenCards);

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        List<DestinationCard> cardsInHand = player.getDestinationCards();
        List<DestinationCard> discardedCards = player.getDiscardDestinationCards();
        List<DestinationCard> cards = cardsInHand;
        cards.addAll(discardedCards);
        gameModel.returnShortDestinationCardsToTheBottom(cards.stream().filter(card -> card.getPoints() < DESTINATION_CARD_TYPE_THRESHOLD).toList());
        gameModel.returnLongDestinationCardsToTheBottom(cards.stream().filter(card -> card.getPoints() >= DESTINATION_CARD_TYPE_THRESHOLD).toList());
        return player.clearDestinationCards();
    }

    @Override
    public boolean resetGame() {
        return !gameModel.isShortDestCardDeckEmpty() && !gameModel.isLongDestCardDeckEmpty();
    }
}
