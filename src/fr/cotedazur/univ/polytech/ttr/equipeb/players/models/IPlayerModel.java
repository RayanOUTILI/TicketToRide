package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;

import java.util.List;

/**
 * Interface to represent the player model in the player engine
 */
public interface IPlayerModel {
    int getNumberOfWagonCards();
    List<WagonCard> getWagonCards(int numberOfCards);
}
