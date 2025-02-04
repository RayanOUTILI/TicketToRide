package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;

import java.util.List;

public interface IDestinationCardsControllerGameModel {
    boolean shuffleDestinationCardsDecks();

    boolean isShortDestCardDeckEmpty();

    boolean isLongDestCardDeckEmpty();

    List<ShortDestinationCard> drawDestinationCards(int maximumCards);
    List<LongDestinationCard> drawLongDestinationCards(int maximumCards);

    void returnShortDestinationCardsToTheBottom(List<ShortDestinationCard> cards);
    void returnLongDestinationCardsToTheBottom(List<LongDestinationCard> cards);
}
