package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutesControllerTrainTest {

    private RoutesController routesController;
    private IRoutesControllerGameModel gameModel;
    private Player player;
    private ClaimRoute claimRoute;
    private Route route;
    private List<WagonCard> wagonCards;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IRoutesControllerGameModel.class);
        when(gameModel.getNbOfPlayers()).thenReturn(3);
        routesController = new RoutesController(gameModel);
        player = mock(Player.class);
        claimRoute = mock(ClaimRoute.class);
        route = mock(Route.class);
        when(route.getId()).thenReturn(1);
        when(route.isClaimed()).thenReturn(false);
        when(route.getLength()).thenReturn(2);
        when(route.getColor()).thenReturn(Color.BLUE);
        when(gameModel.getRoute(1)).thenReturn(route);
        when(claimRoute.route()).thenReturn(route);
        when(player.getNumberOfWagons()).thenReturn(2);
    }

    @org.junit.jupiter.api.Test
    void testNullRoute() {
        when(claimRoute.route()).thenReturn(null);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_INVALID, actionRefused.get());
        verify(claimRoute).route();
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testNullClaimRoute() {
        when(player.askClaimRoute()).thenReturn(null);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_INVALID, actionRefused.get());
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testWrongNumberOfCards() {
        wagonCards = List.of(mock(WagonCard.class));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_WRONG_GIVEN_WAGON_CARDS_SIZE, actionRefused.get());
        verify(claimRoute).wagonCards();
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testRouteAlreadyClaimed() {
        wagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(route.isClaimed()).thenReturn(true);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_WANTED_ROUTE_ALREADY_CLAIMED, actionRefused.get());
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testRouteClaimed() {
        WagonCard wagonCardBlue = mock(WagonCard.class);
        WagonCard wagonCardAny = mock(WagonCard.class);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        when(wagonCardAny.getColor()).thenReturn(Color.ANY);
        wagonCards = List.of(wagonCardBlue, wagonCardAny);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());
        verify(gameModel).discardWagonCards(wagonCards);
        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
        verify(route).setClaimerPlayer(player.getIdentification());
        verify(player).notifyClaimedRoute(route);
    }

    @Test
    void testDoubleRoute() {
        Route doubleRoute = mock(Route.class);
        when(doubleRoute.getId()).thenReturn(2);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        wagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class));
        wagonCards.forEach(card -> when(card.getColor()).thenReturn(Color.BLUE));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);
        when(gameModel.getDoubleRouteOf(1)).thenReturn(doubleRoute);
        when(gameModel.deleteRoute(2)).thenReturn(true);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());

        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
        verify(gameModel).discardWagonCards(wagonCards);
        verify(route).setClaimerPlayer(player.getIdentification());
        verify(gameModel).getDoubleRouteOf(1);
        verify(gameModel).deleteRoute(2);
        verify(gameModel).getNbOfPlayers();
    }

    @Test
    void testDifferentRemovedCardSize() {
        WagonCard cardBlue1 = mock(WagonCard.class);
        WagonCard cardBlue2 = mock(WagonCard.class);
        when(cardBlue1.getColor()).thenReturn(Color.BLUE);
        when(cardBlue2.getColor()).thenReturn(Color.BLUE);
        wagonCards = List.of(cardBlue1, cardBlue2);
        List<WagonCard> removedCards = List.of(cardBlue2);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(removedCards);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_NOT_ENOUGH_WAGON_CARDS, actionRefused.get());
        verify(player).replaceRemovedWagonCards(removedCards);
        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
    }

    @Test
    void testDifferentCardColors() {
        WagonCard cardBlue1 = mock(WagonCard.class);
        WagonCard cardRed = mock(WagonCard.class);
        when(cardBlue1.getColor()).thenReturn(Color.BLUE);
        when(cardRed.getColor()).thenReturn(Color.RED);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        wagonCards = List.of(cardBlue1, cardRed);
        List<WagonCard> removedCards = List.of(cardRed, cardBlue1);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(removedCards);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_TRAIN_WRONG_WAGON_CARDS_COLOR, actionRefused.get());
        verify(player).replaceRemovedWagonCards(removedCards);
        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
    }

    @Test
    void testDifferentCardColorsWithJoker() {
        WagonCard cardBlue1 = mock(WagonCard.class);
        WagonCard cardJoker = mock(WagonCard.class);
        when(route.getType()).thenReturn(RouteType.TRAIN);
        when(cardBlue1.getColor()).thenReturn(Color.BLUE);
        when(cardJoker.getColor()).thenReturn(Color.ANY);
        wagonCards = List.of(cardBlue1, cardJoker);
        List<WagonCard> removedCards = List.of(cardBlue1, cardJoker);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(removedCards);
        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());
        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
    }


}