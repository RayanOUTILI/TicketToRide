package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

public interface IWagonCardsControllerGameModel {
    boolean shuffleWagonCardDeck();

    List<PlayerIdentification> getPlayers();

    boolean isWagonCardDeckEmpty();

    WagonCard drawCardFromWagonCardDeck();

    List<WagonCard> drawCardsFromWagonCardDeck(int numberOfCards);
}
