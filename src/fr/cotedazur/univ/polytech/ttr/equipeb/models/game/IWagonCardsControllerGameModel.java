package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.List;

public interface IWagonCardsControllerGameModel {
    boolean shuffleWagonCardDeck();

    boolean isWagonCardDeckEmpty();

    WagonCard drawCardFromWagonCardDeck();

    List<WagonCard> drawCardsFromWagonCardDeck(int numberOfCards);

    List<WagonCard> getListOfShownWagonCards();

    boolean fillWagonCardDeck();

    boolean removeCardFromShownCards(WagonCard card);

    boolean placeNewWagonCardOnShownCards(WagonCard card);

    boolean placeShownWagonCards(List<WagonCard> wagonCards);

    boolean replaceShownWagonCardsInCaseOfLocomotives(int minimumOfLocomotives);

    boolean discardWagonCards(List<WagonCard> wagonCards);

    boolean clearWagonCardsDeck();
}
