package fr.cotedazur.univ.polytech.ttr.equipeb.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.NonControllableRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModel implements IPlayerGameModel {

    private List<PlayerModel> playerModels;
    // Its needed to change how the WagonCardDeck works
    // Like there is 5 cards showed to the players
    // And the rest of the deck is hidden
    // Also we need to add the discard pile cause when the deck is empty
    // The discard pile is added to the deck
    private WagonCardDeck wagonCardDeck;
    private List<Route> routes;

    public GameModel(List<PlayerModel> playerModels, WagonCardDeck wagonCardDeck, List<Route> routes) {
        this.playerModels = playerModels;
        this.wagonCardDeck = wagonCardDeck;
        this.routes = routes;
    }

    public Set<City> getAllCities(){
        return routes.stream()
                .flatMap(route -> Stream.of(route.getFirstCity(), route.getSecondCity()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public WagonCardDeck getWagonCardDeck() {
        return wagonCardDeck;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public List<NonControllableRoute> getNonControllableRoutes() {
        return new ArrayList<>(routes);
    }
}
