package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectiveBotEngineTest {
    private IPlayerGameModel gameModel;
    private IPlayerModel playerModel;
    private ObjectiveBotEngine botEngine;
    private Map<DestinationCard, List<RouteReadOnly>> routesForObjective;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        gameModel = mock(IPlayerGameModel.class);
        playerModel = mock(IPlayerModel.class);
        when(playerModel.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        IPlayerEngineViewable view = mock(IPlayerEngineViewable.class);
        botEngine = new ObjectiveBotEngine(gameModel, playerModel, view);
        routesForObjective = new HashMap<>();

        Field routesForObjectiveField = ObjectiveBotEngine.class.getDeclaredField("routesForObjective");
        routesForObjectiveField.setAccessible(true);
        routesForObjectiveField.set(botEngine, routesForObjective);
    }

    @Test
    void findDestinationsWith2CommonCitiesInOppositePlaces() {

        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city5 = new City("city5");
        City city6 = new City("city6");

        DestinationCard card1 = new DestinationCard(city1, city2, 10);
        DestinationCard card2 = new DestinationCard(city3, city1, 10);
        DestinationCard card3 = new DestinationCard(city6, city5, 10);

        List<DestinationCard> cards = List.of(card1, card3, card2);
        List<DestinationCard> pair = botEngine.askInitialDestinationCards(cards);
        assertEquals(2, pair.size());
        assertTrue(pair.contains(card1));
        assertTrue(pair.contains(card2));
    }

    @Test
    void findDestinationsWithNoCommonCities(){

        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");
        City city6 = new City("city6");
        City city7 = new City("city7");
        City city8 = new City("city8");

        DestinationCard card1 = new DestinationCard(city1, city2, 9);
        DestinationCard card2 = new DestinationCard(city3, city4, 14);
        DestinationCard card3 = new DestinationCard(city6, city5, 10);
        DestinationCard card4 = new DestinationCard(city7, city8, 8);

        List<DestinationCard> cards = List.of(card1, card2, card4, card3);
        List<DestinationCard> pair = botEngine.askInitialDestinationCards(cards);

        assertEquals(2, pair.size());
        assertTrue(pair.contains(card2));
        assertTrue(pair.contains(card3));
    }

    @Test
    void testAskClaimRoute() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");

        DestinationCard card = new DestinationCard(city1, city4, 10);
        Route route12 = new Route(city1, city2, 6, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 6, RouteType.TRAIN, Color.BLACK, 0);
        Route route34 = new Route(city3, city4, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route45 = new Route(city4, city5, 4, RouteType.TRAIN, Color.BLACK, 0);

        routesForObjective.put(card, List.of(route12, route23, route34, route45));

        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(5);
        when(playerModel.getNumberOfWagons()).thenReturn(5);
        when(playerModel.getNumberOfWagonCards()).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23, route34));

        ClaimObject<RouteReadOnly> claimRoute = botEngine.askClaimRoute();
        assertNotNull(claimRoute);
        assertEquals(route34, claimRoute.getClaimable());
    }

    @Test
    void testAskStation() {
        assertNull(botEngine.askClaimStation());
    }

    @Test
    void testAskDestinationCards() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");
        City city6 = new City("city6");

        DestinationCard dest13 = new DestinationCard(city1, city3, 10);
        DestinationCard dest14 = new DestinationCard(city1, city4, 10);
        DestinationCard dest16 = new DestinationCard(city1, city6, 10);

        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 6, RouteType.TRAIN, Color.BLACK, 0);
        Route route24 = new Route(city2, city4, 6, RouteType.TRAIN, Color.BLACK, 0);
        Route route25 = new Route(city2, city5, 6, RouteType.TRAIN, Color.BLACK, 0);
        Route route56 = new Route(city5, city6, 6, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(List.of(route12,
                route23,
                route24,
                route25,
                route56));
        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of());
        List<DestinationCard> cards = List.of(dest13, dest14, dest16);
        List<DestinationCard> selectedCards = botEngine.askDestinationCards(cards);


        assertEquals(2, selectedCards.size());
        assertEquals(dest13, selectedCards.getFirst());
        assertEquals(dest14, selectedCards.get(1));

        assertTrue(routesForObjective.containsKey(dest13));
        assertTrue(routesForObjective.containsKey(dest14));
        assertFalse(routesForObjective.containsKey(dest16));

        assertEquals(2, routesForObjective.get(dest13).size());
        assertEquals(2, routesForObjective.get(dest14).size());

        assertTrue(routesForObjective.get(dest13).contains(route12));
        assertTrue(routesForObjective.get(dest13).contains(route23));
        assertTrue(routesForObjective.get(dest14).contains(route12));
        assertTrue(routesForObjective.get(dest14).contains(route24));
    }

    @Test
    void testAskDestinationCardsOnlyOne() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");
        City city6 = new City("city6");

        DestinationCard dest13 = new DestinationCard(city1, city3, 10);
        DestinationCard dest14 = new DestinationCard(city1, city4, 10);
        DestinationCard dest16 = new DestinationCard(city1, city6, 10);

        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 8, RouteType.TRAIN, Color.BLACK, 0);
        Route route24 = new Route(city2, city4, 8, RouteType.TRAIN, Color.BLACK, 0);
        Route route25 = new Route(city2, city5, 3, RouteType.TRAIN, Color.BLACK, 0);
        Route route56 = new Route(city5, city6, 3, RouteType.TRAIN, Color.BLACK, 0);

        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(List.of(route12,
                route23,
                route24,
                route25,
                route56));
        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of());
        List<DestinationCard> cards = List.of(dest13, dest14, dest16);
        List<DestinationCard> selectedCards = botEngine.askDestinationCards(cards);

        assertEquals(1, selectedCards.size());
        assertEquals(dest16, selectedCards.getFirst());

        assertFalse(routesForObjective.containsKey(dest13));
        assertFalse(routesForObjective.containsKey(dest14));
        assertTrue(routesForObjective.containsKey(dest16));

        assertEquals(3, routesForObjective.get(dest16).size());

        assertTrue(routesForObjective.get(dest16).contains(route12));
        assertTrue(routesForObjective.get(dest16).contains(route25));
        assertTrue(routesForObjective.get(dest16).contains(route56));
    }

    @Test
    void testAskInitialDestinationCard() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");
        City city6 = new City("city6");
        City city7 = new City("city7");

        DestinationCard card12 = new DestinationCard(city1, city2, 10);
        DestinationCard card31 = new DestinationCard(city3, city1, 10);
        DestinationCard card45 = new DestinationCard(city4, city5, 10);
        DestinationCard card67 = new DestinationCard(city6, city7, 10);

        List<DestinationCard> cards = List.of(card12, card31, card45, card67);

        List<DestinationCard> selectedCards = botEngine.askInitialDestinationCards(cards);

        assertEquals(2, selectedCards.size());
        assertTrue(selectedCards.contains(card12));
        assertTrue(selectedCards.contains(card31));
    }

    @Test
    void testAskInitialDestinationCardNoCommonCities() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");
        City city4 = new City("city4");
        City city5 = new City("city5");
        City city6 = new City("city6");
        City city7 = new City("city7");
        City city8 = new City("city8");

        DestinationCard card12 = new DestinationCard(city1, city2, 10);
        DestinationCard card34 = new DestinationCard(city3, city4, 15);
        DestinationCard card56 = new DestinationCard(city5, city6, 19);
        DestinationCard card78 = new DestinationCard(city7, city8, 10);

        List<DestinationCard> cards = List.of(card12, card34, card56, card78);

        List<DestinationCard> selectedCards = botEngine.askInitialDestinationCards(cards);

        assertEquals(2, selectedCards.size());
        assertTrue(selectedCards.contains(card34));
        assertTrue(selectedCards.contains(card56));
    }

    @Test
    void askWagonCardFromShownCards() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");

        DestinationCard card13 = new DestinationCard(city1, city3, 10);

        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 6, RouteType.TRAIN, Color.GREEN, 0);

        routesForObjective.put(card13, List.of(route12, route23));

        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(0);
        when(playerModel.getNumberOfWagons()).thenReturn(6);
        when(playerModel.getNumberOfWagonCards()).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23));

        List<WagonCard> shownCards = List.of(new WagonCard(Color.BLACK), new WagonCard(Color.GREEN));
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);

        WagonCard selectedCard = botEngine.askWagonCardFromShownCards();
        assertEquals(shownCards.getFirst(), selectedCard);
    }

    @Test
    void testChooseRouteFromCity() {
        assertNull(botEngine.askChooseRouteStation(new City("city")));
    }

    @Test
    void testChooseActionDrawWagonCardShown() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");

        DestinationCard card13 = new DestinationCard(city1, city3, 10);

        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 6, RouteType.TRAIN, Color.GREEN, 0);

        routesForObjective.put(card13, List.of(route12, route23));

        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(0);
        when(playerModel.getNumberOfWagons()).thenReturn(6);
        when(playerModel.getNumberOfWagonCards()).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23));

        List<WagonCard> shownCards = List.of(new WagonCard(Color.BLACK), new WagonCard(Color.BLUE));
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);


        Optional<ActionDrawWagonCard> selectedAction = botEngine.askDrawWagonCard(List.of(ActionDrawWagonCard.DRAW_FROM_DECK, ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS));
        assertEquals(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS, selectedAction.get());
    }

    @Test
    void testChooseActionDrawWagonCardDeck() {
        City city1 = new City("city1");
        City city2 = new City("city2");
        City city3 = new City("city3");

        DestinationCard card13 = new DestinationCard(city1, city3, 10);

        Route route12 = new Route(city1, city2, 5, RouteType.TRAIN, Color.BLACK, 0);
        Route route23 = new Route(city2, city3, 6, RouteType.TRAIN, Color.GREEN, 0);

        routesForObjective.put(card13, List.of(route12, route23));

        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(0);
        when(playerModel.getNumberOfWagons()).thenReturn(6);
        when(playerModel.getNumberOfWagonCards()).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(List.of(route12, route23));

        List<WagonCard> shownCards = List.of(new WagonCard(Color.RED), new WagonCard(Color.BLUE));
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);


        Optional<ActionDrawWagonCard> selectedAction = botEngine.askDrawWagonCard(List.of(ActionDrawWagonCard.DRAW_FROM_DECK, ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS));
        assertEquals(ActionDrawWagonCard.DRAW_FROM_DECK, selectedAction.get());
    }
}