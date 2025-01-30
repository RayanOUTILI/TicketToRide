package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WagonCardsController extends Controller {
    private static final int STARTING_WAGON_CARDS = 4;
    private static final int MAX_SHOWN_WAGON_CARDS = 5;
    private static final int MIN_LOCOMOTIVES_FOR_REPLACE = 3;
    private final IWagonCardsControllerGameModel gameModel;

    public WagonCardsController(IWagonCardsControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean initGame() {
        List<WagonCard> newShownCards = gameModel.drawCardsFromWagonCardDeck(MAX_SHOWN_WAGON_CARDS);
        gameModel.replaceShownWagonCards(newShownCards);
        return gameModel.shuffleWagonCardDeck();
    }

    @Override
    public boolean initPlayer(Player player) {
        if (gameModel.isWagonCardDeckEmpty()) return false;

        List<WagonCard> wagonCards = gameModel.drawCardsFromWagonCardDeck(STARTING_WAGON_CARDS);

        if (wagonCards == null || wagonCards.isEmpty() || wagonCards.size() != STARTING_WAGON_CARDS) return false;

        player.receivedWagonCards(wagonCards);

        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        if (gameModel.isWagonCardDeckEmpty()) return Optional.of(ReasonActionRefused.WAGON_CARDS_DECK_EMPTY);

        boolean hasChosenLocomotive = false;
        for(int round = 1; round <= 2 && !hasChosenLocomotive; round++) {
            List<ActionDrawWagonCard> possibleActions = new ArrayList<>();
            if(!gameModel.isWagonCardDeckEmpty()) possibleActions.add(ActionDrawWagonCard.DRAW_FROM_DECK);
            if(!gameModel.getListOfShownWagonCards().isEmpty()) possibleActions.add(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS);
            Optional<ActionDrawWagonCard> action = player.askDrawWagonCard(possibleActions);

            if(action.isPresent() && action.get() == ActionDrawWagonCard.DRAW_FROM_DECK && possibleActions.contains(action.get())) {
                WagonCard card = gameModel.drawCardFromWagonCardDeck();
                player.receivedWagonCard(card);
            }
            else if (action.isPresent() && action.get() == ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS && possibleActions.contains(action.get())) {
                List<WagonCard> shownCards = gameModel.getListOfShownWagonCards();
                WagonCard chosenCard;
                do {
                     chosenCard = player.askWagonCardFromShownCards();
                } while (chosenCard == null || !shownCards.contains(chosenCard) || !gameModel.removeCardFromShownCards(chosenCard));

                player.receivedWagonCard(chosenCard);

                if(chosenCard.getColor() == Color.ANY) hasChosenLocomotive = true;

                if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();

                WagonCard card = gameModel.drawCardFromWagonCardDeck();

                gameModel.placeNewWagonCardOnShownCards(card);

                while (gameModel.getListOfShownWagonCards().stream().filter(c -> c.getColor() == Color.ANY).count() >= MIN_LOCOMOTIVES_FOR_REPLACE) {
                    List<WagonCard> newShownCards = gameModel.drawCardsFromWagonCardDeck(MAX_SHOWN_WAGON_CARDS);
                    gameModel.replaceShownWagonCards(newShownCards);
                }

            }
            else if(round == 1) return Optional.of(ReasonActionRefused.WAGON_CARDS_ACTION_INVALID);
        }

        return Optional.empty();
    }
}
