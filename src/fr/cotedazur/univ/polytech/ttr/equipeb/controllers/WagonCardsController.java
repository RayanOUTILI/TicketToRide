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
        return gameModel.shuffleWagonCardDeck() && gameModel.getListOfShownWagonCards().size() == MAX_SHOWN_WAGON_CARDS;
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
            List<ActionDrawWagonCard> possibleActions = getPossibleActions();

            Optional<ActionDrawWagonCard> action = player.askDrawWagonCard(possibleActions);

            if(isActionDrawFromDeckAllowed(action, possibleActions)) {
                WagonCard card = gameModel.drawCardFromWagonCardDeck();
                player.receivedWagonCard(card);
            }
            else if (isActionDrawFromShownCardsAllowed(action, possibleActions)) {
                List<WagonCard> shownCards = gameModel.getListOfShownWagonCards();
                WagonCard chosenCard;
                do {
                     chosenCard = player.askWagonCardFromShownCards();
                } while (!isChosenCardAcceptable(chosenCard, shownCards));

                player.receivedWagonCard(chosenCard);

                if(chosenCard.getColor() == Color.ANY) hasChosenLocomotive = true;

                addNewCardToShown();
            }
            else if(round == 1) return Optional.of(ReasonActionRefused.WAGON_CARDS_ACTION_INVALID);
        }

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        List<WagonCard> cards = player.removeWagonCards(player.getWagonCardsHand());
        return gameModel.discardWagonCards(cards);
    }

    @Override
    public boolean resetGame() {
        return gameModel.clearWagonCardsDeck();
    }

    private boolean isActionDrawFromDeckAllowed(Optional<ActionDrawWagonCard> action, List<ActionDrawWagonCard> possibleActions) {
        return action.isPresent() && action.get() == ActionDrawWagonCard.DRAW_FROM_DECK && possibleActions.contains(ActionDrawWagonCard.DRAW_FROM_DECK);
    }

    private boolean isActionDrawFromShownCardsAllowed(Optional<ActionDrawWagonCard> action, List<ActionDrawWagonCard> possibleActions) {
        return action.isPresent() && action.get() == ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS && possibleActions.contains(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS);
    }

    private boolean isChosenCardAcceptable(WagonCard chosenCard, List<WagonCard> shownCards) {
        return chosenCard != null && shownCards.contains(chosenCard) && gameModel.removeCardFromShownCards(chosenCard);
    }

    private List<ActionDrawWagonCard> getPossibleActions() {
        List<ActionDrawWagonCard> possibleActions = new ArrayList<>();

        if(!gameModel.isWagonCardDeckEmpty()) possibleActions.add(ActionDrawWagonCard.DRAW_FROM_DECK);
        if(!gameModel.getListOfShownWagonCards().isEmpty()) possibleActions.add(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS);

        return possibleActions;
    }

    private void addNewCardToShown() {
        boolean filledDeck = true;
        if(gameModel.isWagonCardDeckEmpty()) filledDeck = gameModel.fillWagonCardDeck();

        if(filledDeck) {
            WagonCard card = gameModel.drawCardFromWagonCardDeck();

            gameModel.placeNewWagonCardOnShownCards(card);

            while (gameModel.getListOfShownWagonCards().stream().filter(c -> c.getColor() == Color.ANY).count() >= MIN_LOCOMOTIVES_FOR_REPLACE) {
                List<WagonCard> newShownCards = gameModel.drawCardsFromWagonCardDeck(5);
                gameModel.replaceShownWagonCards(newShownCards);
            }
        }
    }
}
