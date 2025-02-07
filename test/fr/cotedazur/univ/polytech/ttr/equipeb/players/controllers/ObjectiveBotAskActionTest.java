package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.objectivebot.ObjectiveBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectiveBotAskActionTest {
    private IPlayerGameModel gameModel;
    private IPlayerModel playerModel;
    private ObjectiveBotEngine botEngine;
    private Map<DestinationCard, List<RouteReadOnly>> routesForObjective;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        gameModel = mock(IPlayerGameModel.class);
        playerModel = mock(IPlayerModel.class);
        IPlayerEngineViewable view = mock(IPlayerEngineViewable.class);
        botEngine = new ObjectiveBotEngine(gameModel, playerModel, view);
        routesForObjective = new HashMap<>();

        Field routesForObjectiveField = ObjectiveBotEngine.class.getDeclaredField("routesForObjective");
        routesForObjectiveField.setAccessible(true);
        routesForObjectiveField.set(botEngine, routesForObjective);
    }

    @Test
    void testAskActionClaimRoute() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");

        DestinationCard card = new DestinationCard(city1, city4, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route34 = new Route(city3, city4, 5, RouteType.TRAIN, Color.BLACK, 0);

        routesForObjective.put(card, List.of(route12, route23, route34));

        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(5);
        when(playerModel.getNumberOfWagons()).thenReturn(5);
        when(playerModel.getNumberOfWagonCards()).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23, route34));

        assertEquals(Action.CLAIM_ROUTE, botEngine.askAction());
    }

    @Test
    void testAskActionDrawWagonCard() {
        City city1 = new City("city1");
        City city2 = new City("city2");

        DestinationCard card = new DestinationCard(city1, city2, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);

        routesForObjective.put(card, List.of(route12));
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12));
        assertEquals(Action.PICK_WAGON_CARD, botEngine.askAction());
    }

    @Test
    void testAskActionDrawDestinationCards() {
        assertEquals(Action.PICK_DESTINATION_CARDS, botEngine.askAction());
    }

    @Test
    void testAskActionDrawWagonCardLastResort() {
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(true);
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        assertEquals(Action.PICK_WAGON_CARD, botEngine.askAction());
    }

    @Test
    void testAskActionNoMoreMoves() {
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(true);
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(true);
        assertEquals(Action.STOP, botEngine.askAction());
    }

    @Test
    void testUpdateStateOfRoutes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");

        DestinationCard card = new DestinationCard(city1, city4, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route34 = new Route(city3, city4, 5, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23, route34));

        routesForObjective.put(card, List.of(route12, route23, route34));

        route12.setClaimerPlayer(PlayerIdentification.GREEN);

        Method updateStateOfRoutes = ObjectiveBotEngine.class.getDeclaredMethod("updateStateOfRoutes");
        updateStateOfRoutes.setAccessible(true);
        updateStateOfRoutes.invoke(botEngine);

        assertEquals(3, routesForObjective.get(card).size());
        assertTrue(routesForObjective.get(card).getFirst().isClaimed());
        assertFalse(routesForObjective.get(card).get(1).isClaimed());
        assertFalse(routesForObjective.get(card).get(2).isClaimed());
    }

    @Test
    void testCheckRoutesForClaiming() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");

        DestinationCard card = new DestinationCard(city1, city4, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route34 = new Route(city3, city4, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route24 = new Route(city2, city4, 5, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23, route34, route24));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(List.of(route12, route23, route24));
        when(playerModel.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        routesForObjective.put(card, List.of(route12, route23, route34));

        route34.setClaimerPlayer(PlayerIdentification.GREEN);

        Method checkRoutesForClaiming = ObjectiveBotEngine.class.getDeclaredMethod("checkRoutesForClaiming");
        checkRoutesForClaiming.setAccessible(true);

        assertEquals(3, routesForObjective.get(card).size());

        assertEquals(3, routesForObjective.get(card).size());
        assertEquals(route12, routesForObjective.get(card).getFirst());
        assertEquals(route23, routesForObjective.get(card).get(1));
        assertEquals(route34, routesForObjective.get(card).get(2));

        checkRoutesForClaiming.invoke(botEngine);

        assertEquals(2, routesForObjective.get(card).size());
        assertEquals(route12, routesForObjective.get(card).getFirst());
        assertEquals(route24, routesForObjective.get(card).get(1));
    }

    @Test
    void testCheckRoutesForClaimingClaimedARoute() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");

        DestinationCard card = new DestinationCard(city1, city3, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 5, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(List.of(route12, route23));
        when(playerModel.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        routesForObjective.put(card, List.of(route12, route23));

        route23.setClaimerPlayer(PlayerIdentification.BLACK);

        Method checkRoutesForClaiming = ObjectiveBotEngine.class.getDeclaredMethod("checkRoutesForClaiming");
        checkRoutesForClaiming.setAccessible(true);

        assertEquals(2, routesForObjective.get(card).size());

        assertEquals(route12, routesForObjective.get(card).getFirst());
        assertEquals(route23, routesForObjective.get(card).get(1));

        checkRoutesForClaiming.invoke(botEngine);

        assertEquals(1, routesForObjective.get(card).size());
        assertEquals(route12, routesForObjective.get(card).getFirst());
    }

    @Test
    void testCheckRoutesForObjectiveCompletion() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");

        DestinationCard card = new DestinationCard(city1, city4, 10);
        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route34 = new Route(city3, city4, 5, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23, route34));

        routesForObjective.put(card, List.of(route12, route23, route34));

        route12.setClaimerPlayer(PlayerIdentification.BLACK);
        route23.setClaimerPlayer(PlayerIdentification.BLACK);
        route34.setClaimerPlayer(PlayerIdentification.BLACK);

        when(playerModel.getIdentification()).thenReturn(PlayerIdentification.BLACK);

        Method checkRoutesForObjectiveCompletion = ObjectiveBotEngine.class.getDeclaredMethod("checkRoutesForObjectiveCompletion");
        checkRoutesForObjectiveCompletion.setAccessible(true);
        checkRoutesForObjectiveCompletion.invoke(botEngine);

        assertEquals(0, routesForObjective.size());
    }
}