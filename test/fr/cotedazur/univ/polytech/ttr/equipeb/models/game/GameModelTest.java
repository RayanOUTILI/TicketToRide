package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameModelTest {
    private GameModel gameModel;
    private List<PlayerModel> players;
    private WagonCardDeck wagonCardDeck;
    private DestinationCardDeck<ShortDestinationCard> shortDestinationCardDeck;
    private DestinationCardDeck<LongDestinationCard> longDestinationCardDeck;
    private Route route;
    private List<Route> routes;


    @BeforeEach
    void setUp() {
        this.players = List.of(mock(PlayerModel.class));
        this.wagonCardDeck = mock(WagonCardDeck.class);
        this.shortDestinationCardDeck = mock(DestinationCardDeck.class);
        this.longDestinationCardDeck = mock(DestinationCardDeck.class);
        this.route = mock(Route.class);
        this.routes = new ArrayList<>();
        this.routes.add(route);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);
    }

    @Test
    void testIsAllRoutesClaimed() {
        this.routes.forEach(r -> when(r.isClaimed()).thenReturn(true));
        assertTrue(gameModel.isAllRoutesClaimed());
    }

    @Test
    void testShuffleWagonCardDeck() {
        when(wagonCardDeck.shuffle()).thenReturn(true);
        assertTrue(gameModel.shuffleWagonCardDeck());
    }

    @Test
    void testIsWagonCardDeckEmpty() {
        when(wagonCardDeck.isEmpty()).thenReturn(true);
        assertTrue(gameModel.isWagonCardDeckEmpty());
    }

    @Test
    void testDrawWagonCard() {
        WagonCard wagonCard = mock(WagonCard.class);
        when(wagonCardDeck.drawCard()).thenReturn(wagonCard);
        assertEquals(gameModel.drawCardFromWagonCardDeck(), wagonCard);
    }

    @Test
    void testDrawWagonCards() {
        WagonCard wagonCard1 = mock(WagonCard.class);
        WagonCard wagonCard2 = mock(WagonCard.class);
        List<WagonCard> wagonCards = List.of(wagonCard1, wagonCard2);
        when(wagonCardDeck.drawCard()).thenReturn(wagonCard1, wagonCard2, wagonCard1, wagonCard2);
        assertEquals(gameModel.drawCardsFromWagonCardDeck(2), wagonCards);
        assertEquals(gameModel.drawCardsFromWagonCardDeck(2), wagonCards);
    }

    @Test
    void testGetNonControllableRoutes() {
        assertEquals(gameModel.getNonControllableRoutes(), routes);
    }

    @Test
    void testGetRoute() {
        when(route.getId()).thenReturn(1);
        assertEquals(gameModel.getRoute(1), route);
    }

    @Test
    void testIsDestinationCardDeckEmpty() {
        when(shortDestinationCardDeck.isEmpty()).thenReturn(true);
        assertTrue(gameModel.isShortDestCardDeckEmpty());
    }

    @Test
    void testDrawDestinationCards() {
        List<ShortDestinationCard> destinationCards = List.of(mock(ShortDestinationCard.class));
        when(shortDestinationCardDeck.drawCard(1)).thenReturn(destinationCards);
        when(shortDestinationCardDeck.drawCard(3)).thenReturn(destinationCards);
        assertEquals(gameModel.drawDestinationCards(1), destinationCards);
        assertEquals(gameModel.drawDestinationCards(3), destinationCards);
    }

    @Test
    void testReturnShortDestinationCardsToTheBottom() {
        List<ShortDestinationCard> destinationCards = List.of(mock(ShortDestinationCard.class));
        gameModel.returnShortDestinationCardsToTheBottom(destinationCards);
        verify(shortDestinationCardDeck).addCardsAtBottom(destinationCards);
    }

    @Test
    void testGetDoubleRouteOf() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city1");
        City city4 = new City("city2");
        Route route1 = mock(Route.class);
        when(route1.getFirstCity()).thenReturn(city1);
        when(route1.getSecondCity()).thenReturn(city2);
        when(route1.getId()).thenReturn(1);
        Route route2 = mock(Route.class);
        when(route2.getFirstCity()).thenReturn(city3);
        when(route2.getSecondCity()).thenReturn(city4);
        when(route2.getId()).thenReturn(2);

        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);
        assertEquals(gameModel.getDoubleRouteOf(2), route1);
        assertEquals(gameModel.getDoubleRouteOf(1), route2);
    }

    @Test
    void testDeleteRoute() {
        when(route.getId()).thenReturn(1);
        assertTrue(gameModel.deleteRoute(1));
        assertNull(gameModel.getRoute(1));
    }

    @Test
    void testDiscardWagonCards() {
        WagonCard wagonCard = mock(WagonCard.class);
        List<WagonCard> wagonCards = List.of(wagonCard);
        when(wagonCardDeck.addCardToDiscardPile(wagonCards)).thenReturn(true);
        assertTrue(gameModel.discardWagonCards(wagonCards));
    }

    @Test
    void testShuffleDestinationCardDeck() {
        when(shortDestinationCardDeck.shuffle()).thenReturn(true);
        when(longDestinationCardDeck.shuffle()).thenReturn(true);
        assertTrue(gameModel.shuffleDestinationCardsDecks());
    }

    @Test
    void testGetNbOfPlayers() {
        assertEquals(1, gameModel.getNbOfPlayers());
    }

    @Test
    void testSetAllRoutesNotClaimed() {
        when(route.isClaimed()).thenReturn(true);
        gameModel.setAllRoutesNotClaimed();
        routes.forEach(r -> assertTrue(r.isClaimed()));
    }

    @Test
    void testFillWagonCardDeck() {
        when(wagonCardDeck.fillDeck()).thenReturn(true);
        assertTrue(gameModel.fillWagonCardDeck());
    }

    @Test
    void testGetAllRoutesClaimedByPlayer() {
        PlayerIdentification player = PlayerIdentification.GREEN;
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(route1.isClaimed()).thenReturn(true);
        when(route1.getClaimerPlayer()).thenReturn(player);
        when(route2.isClaimed()).thenReturn(false);
        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player);
        assertEquals(1, claimedRoutes.size());
        assertEquals(route1, claimedRoutes.get(0));
    }

    @Test
    void testGetWinnerScore() {
        PlayerModel player1 = mock(PlayerModel.class);
        PlayerModel player2 = mock(PlayerModel.class);
        when(player1.getScore()).thenReturn(10);
        when(player2.getScore()).thenReturn(20);

        gameModel = new GameModel(List.of(player1, player2), wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        PlayerModel winner = gameModel.getWinner();
        assertEquals(player2, winner);
    }

    @Test
    void testGetWinnerNumberOfObjectiveCardsCompleted() {
        PlayerModel player1 = mock(PlayerModel.class);
        PlayerModel player2 = mock(PlayerModel.class);
        when(player1.getScore()).thenReturn(20);
        when(player2.getScore()).thenReturn(20);

        when(player1.getNumberOfCompletedObjectiveCards()).thenReturn(5);
        when(player2.getNumberOfCompletedObjectiveCards()).thenReturn(3);

        gameModel = new GameModel(List.of(player1, player2), wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        PlayerModel winner = gameModel.getWinner();
        assertEquals(player1, winner);
    }

    @Test
    void testGetWinnerRouteLength() {
        PlayerModel player1 = mock(PlayerModel.class);
        PlayerModel player2 = mock(PlayerModel.class);
        when(player1.getScore()).thenReturn(20);
        when(player2.getScore()).thenReturn(20);

        when(player1.getNumberOfCompletedObjectiveCards()).thenReturn(5);
        when(player2.getNumberOfCompletedObjectiveCards()).thenReturn(5);

        when(player1.getLongestContinuousRouteLength()).thenReturn(10);
        when(player2.getLongestContinuousRouteLength()).thenReturn(15);

        gameModel = new GameModel(List.of(player1, player2), wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        PlayerModel winner = gameModel.getWinner();
        assertEquals(player2, winner);
    }

    @Test
    void testGetAllCities() {
        City city1 = mock(City.class);
        City city2 = mock(City.class);
        Route route1 = mock(Route.class);
        when(route1.getFirstCity()).thenReturn(city1);
        when(route1.getSecondCity()).thenReturn(city2);
        routes = List.of(route1);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<City> cities = gameModel.getAllCities();
        assertEquals(2, cities.size());
        assertTrue(cities.contains(city1));
        assertTrue(cities.contains(city2));
    }

    @Test
    void testGetCity() {
        City city = mock(City.class);
        when(city.getId()).thenReturn(1);
        Route routeMocked = mock(Route.class);
        when(routeMocked.getFirstCity()).thenReturn(city);
        when(routeMocked.getSecondCity()).thenReturn(mock(City.class));
        routes = List.of(routeMocked);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);
        City result = gameModel.getCity(1);
        assertEquals(city, result);
    }

    @Test
    void testGetCitiesClaimedByPlayer() {
        PlayerIdentification player = PlayerIdentification.GREEN;
        City city1 = mock(City.class);
        City city2 = mock(City.class);
        when(city1.getOwner()).thenReturn(player);
        when(city2.getOwner()).thenReturn(null);
        Route route1 = mock(Route.class);
        when(route1.getFirstCity()).thenReturn(city1);
        when(route1.getSecondCity()).thenReturn(city2);
        routes = List.of(route1);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<City> cities = gameModel.getCitiesClaimedByPlayer(player);
        assertEquals(1, cities.size());
        assertEquals(city1, cities.get(0));
    }

    @Test
    void testGetNonControllableAdjacentRoutes() {
        City city = mock(City.class);
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(route1.getFirstCity()).thenReturn(city);
        when(route1.getSecondCity()).thenReturn(mock(City.class));
        when(route2.getFirstCity()).thenReturn(mock(City.class));
        when(route2.getSecondCity()).thenReturn(city);
        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<RouteReadOnly> adjacentRoutes = gameModel.getNonControllableAdjacentRoutes(city);
        assertEquals(2, adjacentRoutes.size());
        assertTrue(adjacentRoutes.contains(route1));
        assertTrue(adjacentRoutes.contains(route2));
    }

    @Test
    void testGetAdjacentRoutes() {
        City city = mock(City.class);
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(route1.getFirstCity()).thenReturn(city);
        when(route1.getSecondCity()).thenReturn(mock(City.class));
        when(route2.getFirstCity()).thenReturn(mock(City.class));
        when(route2.getSecondCity()).thenReturn(city);
        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<Route> adjacentRoutes = gameModel.getAdjacentRoutes(city);
        assertEquals(2, adjacentRoutes.size());
        assertTrue(adjacentRoutes.contains(route1));
        assertTrue(adjacentRoutes.contains(route2));
    }

    @Test
    void testGetNonControllableAvailableRoutes() {
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(route1.isClaimed()).thenReturn(false);
        when(route2.isClaimed()).thenReturn(true);
        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAvailableRoutes();
        assertEquals(1, availableRoutes.size());
        assertTrue(availableRoutes.contains(route1));
    }

    @Test
    void testGetNonControllableAvailableRoutesWithMaxLength() {
        Route route1 = mock(Route.class);
        Route route2 = mock(Route.class);
        when(route1.isClaimed()).thenReturn(false);
        when(route1.getLength()).thenReturn(3);
        when(route2.isClaimed()).thenReturn(false);
        when(route2.getLength()).thenReturn(5);
        routes = List.of(route1, route2);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAvailableRoutes(4);
        assertEquals(1, availableRoutes.size());
        assertTrue(availableRoutes.contains(route1));
    }

    @Test
    void testGetNonControllableAvailableCities() {
        City city1 = mock(City.class);
        City city2 = mock(City.class);
        Route route1 = mock(Route.class);
        when(route1.isClaimed()).thenReturn(false);
        when(route1.getFirstCity()).thenReturn(city1);
        when(route1.getSecondCity()).thenReturn(city2);
        routes = List.of(route1);
        gameModel = new GameModel(players, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

        List<CityReadOnly> availableCities = gameModel.getNonControllableAvailableCities();
        assertEquals(2, availableCities.size());
        assertTrue(availableCities.contains(city1));
        assertTrue(availableCities.contains(city2));
    }
}
