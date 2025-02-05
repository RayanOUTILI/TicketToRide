package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;

public interface IPlayerModelStats {
    int getScore();
    int getNumberOfWagonCards();
    List<DestinationCard> getDestinationCards();
    PlayerType getPlayerType();
    PlayerIdentification getIdentification();
    List<RouteReadOnly> getSelectedStationRoutes();
}
