package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

public interface IWagonCardsControllerGameModel {
    boolean isWagonCardDeckEmpty();

    WagonCard drawCardFromWagonCardDeck();
}
