package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;
import java.util.Optional;

public class WagonCardsController extends Controller {
    private static final int STARTING_WAGON_CARDS = 4;
    private final IWagonCardsControllerGameModel gameModel;

    public WagonCardsController(IWagonCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean init(Player player) {
        if (gameModel.isWagonCardDeckEmpty()) return false;

        boolean shuffled = gameModel.shuffleWagonCardDeck();

        if (!shuffled) return false;

        List<WagonCard> wagonCards = gameModel.drawCardsFromWagonCardDeck(STARTING_WAGON_CARDS);

        if (wagonCards == null || wagonCards.isEmpty() || wagonCards.size() != STARTING_WAGON_CARDS) return false;

        player.receivedWagonCards(wagonCards);

        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        if (gameModel.isWagonCardDeckEmpty()) return Optional.of(ReasonActionRefused.WAGON_CARDS_DECK_EMPTY);

        player.receivedWagonCard(gameModel.drawCardFromWagonCardDeck());

        return Optional.empty();
    }
}
