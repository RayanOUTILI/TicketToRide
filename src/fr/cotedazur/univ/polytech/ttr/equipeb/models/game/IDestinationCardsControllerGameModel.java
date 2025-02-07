package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;

import java.util.List;

public interface IDestinationCardsControllerGameModel {
    boolean shuffleDestinationCardsDecks();

    boolean isShortDestCardDeckEmpty();

    boolean isLongDestCardDeckEmpty();

    List<DestinationCard> drawDestinationCards(int maximumCards);
    List<DestinationCard> drawLongDestinationCards(int maximumCards);

    void returnShortDestinationCardsToTheBottom(List<DestinationCard> cards);
    void returnLongDestinationCardsToTheBottom(List<DestinationCard> cards);

    boolean isShortDestCardDeckFull();
    boolean isLongDestCardDeckFull();
}
