package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;

import java.util.List;

/**
 * Interface to represent the player model in the player engine
 */
public interface IPlayerModel {
    int getNumberOfWagonCards();
    List<WagonCard> getWagonCards(int numberOfCards);
    List<WagonCard> getWagonCardsIncludingAnyColor(Color color, int numberOfCards);
    int getNumberOfWagonCardsIncludingAnyColor(Color color);
}
