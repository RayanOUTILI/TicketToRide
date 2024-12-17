package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModel implements IPlayerGameModel, IRoutesControllerGameModel, IVictoryControllerGameModel, IWagonCardsControllerGameModel, IDestinationCardsControllerGameModel {

    private List<PlayerModel> playerModels;
    // Its needed to change how the WagonCardDeck works
    // Like there is 5 cards showed to the players
    // And the rest of the deck is hidden
    // Also we need to add the discard pile cause when the deck is empty
    // The discard pile is added to the deck
    private WagonCardDeck wagonCardDeck;
    private DestinationCardDeck destinationCardDeck;
    private List<Route> routes;

    public GameModel(List<PlayerModel> playerModels, WagonCardDeck wagonCardDeck, DestinationCardDeck destinationCardDeck, List<Route> routes) {
        this.playerModels = playerModels;
        this.wagonCardDeck = wagonCardDeck;
        this.destinationCardDeck = destinationCardDeck;
        this.routes = routes;
    }

    public Set<City> getAllCities(){
        return routes.stream()
                .flatMap(route -> Stream.of(route.getFirstCity(), route.getSecondCity()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean isAllRoutesClaimed() {
        return routes.stream().allMatch(Route::isClaimed);
    }

    @Override
    public boolean isWagonCardDeckEmpty() {
        return wagonCardDeck.isEmpty();
    }

    @Override
    public WagonCard drawCardFromWagonCardDeck() {
        return wagonCardDeck.drawCard();
    }

    @Override
    public List<RouteReadOnly> getNonControllableRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public Route getRoute(int id) {
        return routes.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean isDestinationCardDeckEmpty() {
        return destinationCardDeck.isEmpty();
    }

    @Override
    public List<DestinationCard> drawDestinationCards(int maximumCards) {
        return destinationCardDeck.drawCard(maximumCards);
    }

    @Override
    public void returnDestinationCardsToTheBottom(List<DestinationCard> cards) {
        destinationCardDeck.addCardsAtBottom(cards);
    }
}
