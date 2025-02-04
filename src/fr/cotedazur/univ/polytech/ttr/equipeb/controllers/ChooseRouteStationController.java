package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IChooseRouteStationControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChooseRouteStationController extends Controller{

    private final IChooseRouteStationControllerGameModel gameModel;

    public ChooseRouteStationController(IChooseRouteStationControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean initGame() {
        return true;
    }

    @Override
    public boolean initPlayer(Player player) {
        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        List<City> claimedCities = gameModel.getCitiesClaimedByPlayer(player.getIdentification());

        if (claimedCities.isEmpty()) {
            return Optional.empty();
        }

        List<RouteReadOnly> chosenRoutes = new ArrayList<>();

        for (City city : claimedCities) {
            RouteReadOnly playerChosenRoute = player.askChooseRouteStation(city);
            if (playerChosenRoute == null) {
                return Optional.of(ReasonActionRefused.ROUTE_INVALID);
            }

            List<RouteReadOnly> adjacentRoutes = new ArrayList<>(gameModel.getAdjacentRoutes(city));

            if (!adjacentRoutes.contains(playerChosenRoute)) {
                return Optional.of(ReasonActionRefused.CHOSEN_ROUTE_NOT_CONNECTED_TO_STATION);
            }

            chosenRoutes.add(playerChosenRoute);
        }

        chosenRoutes.forEach(player::addChosenRouteStation);

        return Optional.empty();
    }

    @Override
    public boolean resetGame() {
        return true;
    }

    @Override
    public boolean resetPlayer(Player player) {
        return player.clearChosenRouteStations();
    }
}
