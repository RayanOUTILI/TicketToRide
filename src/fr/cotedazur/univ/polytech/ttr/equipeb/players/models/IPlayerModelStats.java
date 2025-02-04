package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;

public interface IPlayerModelStats {
    int getScore();
    int getNumberOfWagonCards();
    List<DestinationCard> getDestinationCardsHand();
    PlayerType getPlayerType();
    PlayerIdentification getPlayerIdentification();
    List<RouteReadOnly> getSelectedStationRoutes();
}
