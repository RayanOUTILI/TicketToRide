package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void setUp() {
        playerModel = mock(IPlayerModel.class);
        gameModel = mock(IPlayerGameModel.class);
        random = mock(RandomGenerator.class);
        view = mock(IPlayerEngineViewable.class);

        easyBotEngine = new EasyBotEngine(playerModel, gameModel, view, random);
    }

    @Test
    void testAskActionPickDestinationCards() {
        when(random.nextInt(anyInt())).thenReturn(0);
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(false);

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
        List<ShortDestinationCard> cards = new ArrayList<>(List.of(
                mock(ShortDestinationCard.class),
                mock(ShortDestinationCard.class),
                mock(ShortDestinationCard.class)
        ));

        when(random.nextInt(anyInt())).thenReturn(1);
        when(random.nextInt(anyInt())).thenReturn(1);

        List<ShortDestinationCard> result = easyBotEngine.askDestinationCards(cards);

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
}