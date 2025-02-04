package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class EasyBotEngineTest {
    private EasyBotEngine easyBotEngine;
    private IPlayerModel playerModel;
    private IPlayerGameModel gameModel;
    private IPlayerEngineViewable view;
    private RandomGenerator random;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        playerModel = mock(IPlayerModel.class);
        gameModel = mock(IPlayerGameModel.class);
        random = mock(RandomGenerator.class);
        view = mock(IPlayerEngineViewable.class);

        easyBotEngine = new EasyBotEngine(playerModel, gameModel, view);

        Field randomField = BotModelControllable.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(easyBotEngine, random);
    }

    @Test
    void testAskActionPickDestinationCards() {
        when(random.nextInt(anyInt())).thenReturn(0);
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);

        assertEquals(Action.PICK_DESTINATION_CARDS, easyBotEngine.askAction());
    }

    @Test
    void testAskActionClaimRoute() {
        when(random.nextInt(2)).thenReturn(1);
        when(playerModel.getNumberOfWagonCards()).thenReturn(1);
        Route route = mock(Route.class);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        when(route.isClaimed()).thenReturn(false);
        when(route.getColor()).thenReturn(Color.BLUE);
        when(route.getLength()).thenReturn(1);
        List<RouteReadOnly> routes = new ArrayList<>(List.of(route));
        WagonCard card = mock(WagonCard.class);
        when(card.getColor()).thenReturn(Color.BLUE);
        when(playerModel.getNumberOfWagons()).thenReturn(1);
        when(playerModel.getWagonCards(anyInt())).thenReturn(List.of(card));
        when(playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0)).thenReturn(List.of(card));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(routes);

        assertEquals(Action.CLAIM_ROUTE, easyBotEngine.askAction());
    }

    @Test
    void testAskClaimRouteFerry() {
        Route route = mock(Route.class);
        when(route.isClaimed()).thenReturn(false);
        when(route.getColor()).thenReturn(Color.BLUE);
        when(route.getLength()).thenReturn(1);
        when(route.getType()).thenReturn(RouteType.FERRY);
        when(route.isClaimed()).thenReturn(false);
        when(route.getNbLocomotives()).thenReturn(1);

        WagonCard card = mock(WagonCard.class);
        when(card.getColor()).thenReturn(Color.ANY);
        when(playerModel.getNumberOfWagons()).thenReturn(1);
        when(playerModel.getWagonCards(anyInt())).thenReturn(List.of(card));
        when(playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 1)).thenReturn(List.of(card));


        List<RouteReadOnly> routes = new ArrayList<>(List.of(route));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(routes);
        when(random.nextInt(anyInt())).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(routes);

        ClaimRoute claimRoute = easyBotEngine.askClaimRoute();
        assertEquals(route, claimRoute.route());
        assertEquals(List.of(card), claimRoute.wagonCards());
    }

    @Test
    void testAskClaimRouteTunnel() {
        Route route = mock(Route.class);
        when(route.isClaimed()).thenReturn(false);
        when(route.getColor()).thenReturn(Color.BLUE);
        when(route.getLength()).thenReturn(1);
        when(route.getType()).thenReturn(RouteType.TUNNEL);
        when(route.isClaimed()).thenReturn(false);

        WagonCard card = mock(WagonCard.class);
        when(card.getColor()).thenReturn(Color.ANY);
        when(playerModel.getNumberOfWagons()).thenReturn(1);
        when(playerModel.getWagonCards(anyInt())).thenReturn(List.of(card));
        when(playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0)).thenReturn(List.of(card));


        List<RouteReadOnly> routes = new ArrayList<>(List.of(route));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(routes);
        when(random.nextInt(anyInt())).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(routes);

        ClaimRoute claimRoute = easyBotEngine.askClaimRoute();
        assertEquals(route, claimRoute.route());
        assertEquals(List.of(card), claimRoute.wagonCards());
    }

    @Test
    void testAskActionPickWagonCard() {
        when(random.nextInt(2)).thenReturn(1);
        when(gameModel.getNonControllableAvailableRoutes(anyInt())).thenReturn(new ArrayList<>());

        assertEquals(Action.PICK_WAGON_CARD, easyBotEngine.askAction());
    }

    @Test
    void testAskClaimRoute() {
        Route route = mock(Route.class);
        when(route.isClaimed()).thenReturn(false);
        when(route.getColor()).thenReturn(Color.BLUE);
        when(route.getLength()).thenReturn(1);
        WagonCard card = mock(WagonCard.class);
        when(card.getColor()).thenReturn(Color.BLUE);
        when(playerModel.getNumberOfWagons()).thenReturn(1);
        when(playerModel.getWagonCards(anyInt())).thenReturn(List.of(card));
        when(playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0)).thenReturn(List.of(card));
        when(route.getType()).thenReturn(RouteType.TRAIN);
        List<RouteReadOnly> routes = new ArrayList<>(List.of(route));
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(routes);
        when(random.nextInt(anyInt())).thenReturn(0);
        when(gameModel.getNonControllableRoutes()).thenReturn(routes);

        assertNotNull(easyBotEngine.askClaimRoute());
    }

    @Test
    void testAskDestinationCards() {
        List<DestinationCard> cards = new ArrayList<>(List.of(
                mock(DestinationCard.class),
                mock(DestinationCard.class),
                mock(DestinationCard.class)
        ));

        when(random.nextInt(anyInt())).thenReturn(1);
        when(random.nextInt(anyInt())).thenReturn(1);

        List<DestinationCard> result = easyBotEngine.askDestinationCards(cards);

        assertEquals(2, result.size());
        assertTrue(cards.containsAll(result));
    }

    @Test
    void testActionRefused() {
        easyBotEngine.actionRefused(Action.PICK_DESTINATION_CARDS, ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);
        verify(view).displayActionRefused(Action.PICK_DESTINATION_CARDS, ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);
    }

    @Test
    void testActionCompleted() {
        easyBotEngine.actionCompleted(Action.PICK_DESTINATION_CARDS);
        verify(view).displayActionCompleted(Action.PICK_DESTINATION_CARDS);
    }

    @Test
    void testAskWagonCardFromShownCards(){
        WagonCard card1 = new WagonCard(Color.BLACK);
        WagonCard card2 = new WagonCard(Color.RED);
        WagonCard card3 = new WagonCard(Color.GREEN);
        List<WagonCard> shownCards = new ArrayList<>(List.of(card1, card2, card3));

        when(random.nextInt(anyInt())).thenReturn(1);
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);
        assertEquals(card2, easyBotEngine.askWagonCardFromShownCards());
    }

    @Test
    void testAskChooseRouteStation() {
        City paris = new City("Paris");
        RouteReadOnly parisToLyon = new Route(paris, new City("Lyon"), 1, RouteType.TRAIN, Color.BLACK,  1);
        RouteReadOnly parisToMarseille = new Route(paris, new City("Marseille"), 1, RouteType.TRAIN, Color.BLACK,  1);
        List<RouteReadOnly> routes = new ArrayList<>(List.of(parisToLyon, parisToMarseille));
        when(gameModel.getNonControllableAdjacentRoutes(paris)).thenReturn(routes);
        when(random.nextInt(anyInt())).thenReturn(0);

        assertEquals(parisToLyon, easyBotEngine.askChooseRouteStation(paris));
    }

    @Test
    void testAskCityToPlaceStation() {
        City paris = new City("Paris");
        when(gameModel.getNonControllableAvailableCities()).thenReturn(List.of(paris));
        when(random.nextInt(anyInt())).thenReturn(0);
        WagonCard card1 = new WagonCard(Color.BLACK);
        WagonCard card2 = new WagonCard(Color.BLACK);
        WagonCard card3 = new WagonCard(Color.BLACK);
        when(playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft() - 1))).thenReturn(List.of(card1, card2, card3));
        assertEquals(paris, easyBotEngine.askClaimStation().city());
    }

    @Test
    void tesAskDrawWagonCard(){
        List<ActionDrawWagonCard> possibleActions = new ArrayList<>();
        assertEquals(Optional.empty(), easyBotEngine.askDrawWagonCard(possibleActions));
        possibleActions = new ArrayList<>(List.of(ActionDrawWagonCard.DRAW_FROM_DECK, ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS));
        when(random.nextInt(anyInt())).thenReturn(0);
        assertEquals(Optional.of(ActionDrawWagonCard.DRAW_FROM_DECK), easyBotEngine.askDrawWagonCard(possibleActions));
        when(random.nextInt(anyInt())).thenReturn(1);
        assertEquals(Optional.of(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS), easyBotEngine.askDrawWagonCard(possibleActions));
    }
}