package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
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
    private DestinationCardDeck destinationCardDeck;
    private Route route;
    private List<Route> routes;


    @BeforeEach
    void setUp() {
        this.players = List.of(mock(PlayerModel.class));
        this.wagonCardDeck = mock(WagonCardDeck.class);
        this.destinationCardDeck = mock(DestinationCardDeck.class);
        this.route = mock(Route.class);
        this.routes = new ArrayList<>();
        this.routes.add(route);
        gameModel = new GameModel(players, wagonCardDeck, destinationCardDeck, routes);
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
        when(destinationCardDeck.isEmpty()).thenReturn(true);
        assertTrue(gameModel.isDestinationCardDeckEmpty());
    }

    @Test
    void testDrawDestinationCards() {
        List<ShortDestinationCard> destinationCards = List.of(mock(ShortDestinationCard.class));
        when(destinationCardDeck.drawCard(1)).thenReturn(destinationCards);
        when(destinationCardDeck.drawCard(3)).thenReturn(destinationCards);
        assertEquals(gameModel.drawDestinationCards(1), destinationCards);
        assertEquals(gameModel.drawDestinationCards(3), destinationCards);
    }

    @Test
    void testReturnDestinationCardsToTheBottom() {
        List<ShortDestinationCard> destinationCards = List.of(mock(ShortDestinationCard.class));
        gameModel.returnDestinationCardsToTheBottom(destinationCards);
        verify(destinationCardDeck).addCardsAtBottom(destinationCards);
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
        gameModel = new GameModel(players, wagonCardDeck, destinationCardDeck, routes);
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
        when(destinationCardDeck.shuffle()).thenReturn(true);
        assertTrue(gameModel.shuffleDestinationCardDeck());
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
}
