package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;

import java.util.List;

public interface IDestinationCardsControllerGameModel {
    boolean shuffleDestinationCardDeck();

    boolean isDestinationCardDeckEmpty();

    List<ShortDestinationCard> drawDestinationCards(int maximumCards);

    void returnDestinationCardsToTheBottom(List<ShortDestinationCard> cards);
}
