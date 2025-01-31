package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

public interface IChooseRouteStationControllerGameModel {
    List<City> getCitiesClaimedByPlayer(PlayerIdentification player);
    List<Route> getAdjacentRoutes(City city);
}
