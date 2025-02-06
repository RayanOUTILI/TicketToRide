package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.randombots.BotEngineWithRandom;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.randombots.MediumBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediumBotEngineTest {

    private IPlayerGameModel gameModel;
    private IPlayerModel playerModel;
    private RandomGenerator random;
    private MediumBotEngine botEngine;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        gameModel = mock(IPlayerGameModel.class);
        playerModel = mock(IPlayerModel.class);
        IPlayerEngineViewable view = mock(IPlayerEngineViewable.class);
        random = mock(RandomGenerator.class);
        botEngine = new MediumBotEngine(gameModel, playerModel, view);

        Field randomField = BotEngineWithRandom.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(botEngine, random);
    }

    @Test
    void askAction_claimRoute() {
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(Collections.singletonList(mock(RouteReadOnly.class)));

        when(playerModel.getNumberOfWagonCards()).thenReturn(16);
        when(playerModel.getNumberOfWagons()).thenReturn(20);
        when(gameModel.getNonControllableAvailableRoutes().getFirst().isClaimed()).thenReturn(false);
        when(gameModel.getNonControllableAvailableRoutes().getFirst().getLength()).thenReturn(5);
        when(gameModel.getNonControllableAvailableRoutes().getFirst().getColor()).thenReturn(Color.BLACK);
        when(gameModel.getNonControllableAvailableRoutes().getFirst().getType()).thenReturn(RouteType.TRAIN);
        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLACK)).thenReturn(5);
        assertEquals(Action.CLAIM_ROUTE, botEngine.askAction());
    }

    @Test
    void askAction_placeStation() {
        when(playerModel.getStationsLeft()).thenReturn(1);
        when(playerModel.getNumberOfWagonCards()).thenReturn(16);
        when(playerModel.getWagonCardsIncludingAnyColor(anyInt())).thenReturn(Arrays.asList(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));

        assertEquals(Action.PLACE_STATION, botEngine.askAction());
    }


    @Test
    void askAction_pickDestinationCards() {
        when(playerModel.getDestinationCards()).thenReturn(Collections.emptyList());
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(playerModel.getNumberOfWagonCards()).thenReturn(16);

        assertEquals(Action.PICK_DESTINATION_CARDS, botEngine.askAction());
    }

    @Test
    void askAction_pickWagonCard() {
        when(playerModel.getDestinationCards()).thenReturn(Arrays.asList(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class)));
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);

        assertEquals(Action.PICK_WAGON_CARD, botEngine.askAction());
    }

    @Test
    void askClaimRoute_noAvailableRoute() {
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(Collections.emptyList());

        assertNull(botEngine.askClaimRoute());
    }

    @Test
    void askClaimRoute_bestRoute() {
        List<RouteReadOnly> availableRoutes = createRoutesOfAllLengths();
        RouteReadOnly routeLength8 = availableRoutes.get(6);

        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(availableRoutes);
        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(any())).thenReturn(8);
        when(playerModel.getNumberOfWagons()).thenReturn(8);

        ClaimObject<RouteReadOnly> claimRoute = botEngine.askClaimRoute();
        assertNotNull(claimRoute);
        assertEquals(routeLength8, claimRoute.getClaimable());
    }

    private List<RouteReadOnly> createRoutesOfAllLengths() {
        RouteReadOnly routeLength1 = mock(RouteReadOnly.class);
        when(routeLength1.isClaimed()).thenReturn(false);
        when(routeLength1.getLength()).thenReturn(1);
        when(routeLength1.getColor()).thenReturn(Color.BLACK);
        when(routeLength1.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength2 = mock(RouteReadOnly.class);
        when(routeLength2.isClaimed()).thenReturn(false);
        when(routeLength2.getLength()).thenReturn(2);
        when(routeLength2.getColor()).thenReturn(Color.BLACK);
        when(routeLength2.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength3 = mock(RouteReadOnly.class);
        when(routeLength3.isClaimed()).thenReturn(false);
        when(routeLength3.getLength()).thenReturn(3);
        when(routeLength3.getColor()).thenReturn(Color.BLACK);
        when(routeLength3.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength5 = mock(RouteReadOnly.class);
        when(routeLength5.isClaimed()).thenReturn(false);
        when(routeLength5.getLength()).thenReturn(5);
        when(routeLength5.getColor()).thenReturn(Color.BLACK);
        when(routeLength5.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength8 = mock(RouteReadOnly.class);
        when(routeLength8.isClaimed()).thenReturn(false);
        when(routeLength8.getLength()).thenReturn(8);
        when(routeLength8.getColor()).thenReturn(Color.BLACK);
        when(routeLength8.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength6 = mock(RouteReadOnly.class);
        when(routeLength6.isClaimed()).thenReturn(false);
        when(routeLength6.getLength()).thenReturn(6);
        when(routeLength6.getColor()).thenReturn(Color.BLACK);
        when(routeLength6.getType()).thenReturn(RouteType.TRAIN);

        RouteReadOnly routeLength4 = mock(RouteReadOnly.class);
        when(routeLength4.isClaimed()).thenReturn(false);
        when(routeLength4.getLength()).thenReturn(4);
        when(routeLength4.getColor()).thenReturn(Color.BLACK);
        when(routeLength4.getType()).thenReturn(RouteType.TRAIN);

        return List.of(routeLength1, routeLength2, routeLength3, routeLength4, routeLength5, routeLength6, routeLength8);
    }

    @Test
    void askClaimStation_noAvailableCity() {
        when(gameModel.getNonControllableAvailableCities()).thenReturn(Collections.emptyList());

        assertNull(botEngine.askClaimStation());
    }

    @Test
    void askClaimStation_bestCity() {
        CityReadOnly city = mock(CityReadOnly.class);
        when(gameModel.getNonControllableAvailableCities()).thenReturn(Collections.singletonList(city));
        when(playerModel.getWagonCardsIncludingAnyColor(anyInt())).thenReturn(Arrays.asList(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));

        ClaimObject<CityReadOnly> claimStation = botEngine.askClaimStation();
        assertNotNull(claimStation);
        assertEquals(city, claimStation.getClaimable());
    }

    @Test
    void askDestinationCards_prioritizeCards() {
        DestinationCard card1 = mock(DestinationCard.class);
        DestinationCard card2 = mock(DestinationCard.class);
        DestinationCard card3 = mock(DestinationCard.class);
        when(card1.getPoints()).thenReturn(5);
        when(card2.getPoints()).thenReturn(10);
        when(card3.getPoints()).thenReturn(15);

        List<DestinationCard> selectedCards = botEngine.askDestinationCards(Arrays.asList(card1, card2, card3));
        assertEquals(2, selectedCards.size());
        assertTrue(selectedCards.contains(card2));
        assertTrue(selectedCards.contains(card3));
    }

    @Test
    void askDrawWagonCard_randomSelection() {
        when(random.nextInt(anyInt())).thenReturn(1);

        Optional<ActionDrawWagonCard> selectedAction = botEngine.askDrawWagonCard(Arrays.asList(ActionDrawWagonCard.DRAW_FROM_DECK, ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS));
        assertTrue(selectedAction.isPresent());
        assertEquals(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS, selectedAction.get());
    }

    @Test
    void askWagonCardFromShownCards_randomSelection() {
        WagonCard card1 = mock(WagonCard.class);
        WagonCard card2 = mock(WagonCard.class);
        when(gameModel.getListOfShownWagonCards()).thenReturn(Arrays.asList(card1, card2));
        when(random.nextInt(anyInt())).thenReturn(0);

        WagonCard selectedCard = botEngine.askWagonCardFromShownCards();
        assertEquals(card1, selectedCard);
    }

    @Test
    void testAskChooseRouteStation() {
        City paris = new City("Nice");
        RouteReadOnly niceToLyon = new Route(paris, new City("Lyon"), 1, RouteType.TRAIN, Color.BLACK,  1);
        RouteReadOnly parisToMarseille = new Route(paris, new City("Marseille"), 1, RouteType.TRAIN, Color.BLACK,  1);
        List<RouteReadOnly> routes = new ArrayList<>(List.of(niceToLyon, parisToMarseille));
        when(gameModel.getNonControllableAdjacentRoutes(paris)).thenReturn(routes);
        when(random.nextInt(anyInt())).thenReturn(0);

        assertEquals(niceToLyon, botEngine.askChooseRouteStation(paris));
    }

    @Test
    void testReset() {
        assertTrue(botEngine.reset());
    }
}