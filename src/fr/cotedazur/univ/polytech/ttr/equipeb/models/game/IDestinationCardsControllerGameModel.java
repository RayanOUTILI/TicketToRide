package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.List;

public interface IDestinationCardsControllerGameModel {
    boolean isDestinationCardDeckEmpty();

    List<DestinationCard> drawDestinationCards(int maximumCards);

    void returnDestinationCardsToTheBottom(List<DestinationCard> cards);
}
