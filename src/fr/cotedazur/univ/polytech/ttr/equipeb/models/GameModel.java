package fr.cotedazur.univ.polytech.ttr.equipeb.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.Card;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModel {

    List<Player> players;
    // Its needed to change how the WagonCardDeck works
    // Like there is 5 cards showed to the players
    // And the rest of the deck is hidden
    // Also we need to add the discard pile cause when the deck is empty
    // The discard pile is added to the deck
    WagonCardDeck wagonCardDeck;
    List<Route> routes;

    public Set<City> getAllCities(){
        return routes.stream()
                .flatMap(route -> Stream.of(route.getCity1(), route.getCity2()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
