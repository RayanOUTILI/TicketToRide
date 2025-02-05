package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediumBotEngineTest {

    private IPlayerGameModel gameModel;
    private IPlayerModel playerModel;
    private IPlayerEngineViewable view;
    private RandomGenerator random;
    private MediumBotEngine botEngine;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        gameModel = mock(IPlayerGameModel.class);
        playerModel = mock(IPlayerModel.class);
        view = mock(IPlayerEngineViewable.class);
        random = mock(RandomGenerator.class);
        botEngine = new MediumBotEngine(playerModel, gameModel, view);

        Field randomField = MediumBotEngine.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(botEngine, random);
    }

    @Test
    void askAction_claimRoute() {
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(Collections.singletonList(mock(RouteReadOnly.class)));

        when(playerModel.getNumberOfWagonCards()).thenReturn(16);
        when(playerModel.getNumberOfWagons()).thenReturn(20);
        when(gameModel.getNonControllableAvailableRoutes().get(0).isClaimed()).thenReturn(false);
        when(gameModel.getNonControllableAvailableRoutes().get(0).getLength()).thenReturn(5);
        when(gameModel.getNonControllableAvailableRoutes().get(0).getColor()).thenReturn(Color.BLACK);
        when(gameModel.getNonControllableAvailableRoutes().get(0).getType()).thenReturn(RouteType.TRAIN);
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
        RouteReadOnly route = mock(RouteReadOnly.class);
        when(route.isClaimed()).thenReturn(false);
        when(route.getLength()).thenReturn(5);
        when(route.getColor()).thenReturn(Color.BLACK);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        when(gameModel.getNonControllableAvailableRoutes()).thenReturn(Collections.singletonList(route));
        when(playerModel.getNumberOfWagonCardsIncludingAnyColor(any())).thenReturn(5);
        when(playerModel.getNumberOfWagons()).thenReturn(5);

        ClaimObject<RouteReadOnly> claimRoute = botEngine.askClaimRoute();
        assertNotNull(claimRoute);
        assertEquals(route, claimRoute.getClaimable());
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
}