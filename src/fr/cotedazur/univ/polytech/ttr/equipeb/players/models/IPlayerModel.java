package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;

import java.util.List;

/**
 * THIS INTERFACE IS USED BY THE PLAYER ENGINE TO CONTROL THE PLAYER ACTIONS
 * AND GET THE PLAYER MODEL INFOS
 */
public interface IPlayerModel {
    int getNumberOfWagonCards();
    List<DestinationCard> getDestinationCards();
    int getStationsLeft();
    List<WagonCard> getWagonCards(int numberOfCards);
    List<WagonCard> getWagonCardsIncludingAnyColor(int numberOfCards);
    List<WagonCard> getWagonCardsIncludingAnyColor(Color color, int numberOfCards, int numberLocomotives);
    List<WagonCard> getWagonCardsOfColor(Color color, int numberOfCards);

    int getNumberOfWagons();
    PlayerIdentification getPlayerIdentification();
}
