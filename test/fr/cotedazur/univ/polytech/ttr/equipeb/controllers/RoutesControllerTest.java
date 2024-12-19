package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteColor;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutesControllerTest {

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
        when(gameModel.getRoute(1)).thenReturn(route);
        when(claimRoute.route()).thenReturn(route);

    }

    @org.junit.jupiter.api.Test
    void testNullRoute() {
        when(claimRoute.route()).thenReturn(null);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        assertFalse(routesController.doAction(player));
        verify(claimRoute).route();
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testNullClaimRoute() {
        when(player.askClaimRoute()).thenReturn(null);
        assertFalse(routesController.doAction(player));
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testWrongNumberOfCards() {
        wagonCards = List.of(mock(WagonCard.class));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        assertFalse(routesController.doAction(player));
        verify(claimRoute).wagonCards();
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testRouteAlreadyClaimed() {
        wagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(route.isClaimed()).thenReturn(true);
        assertFalse(routesController.doAction(player));
        verify(player).askClaimRoute();
    }

    @org.junit.jupiter.api.Test
    void testRouteClaimed() {
        wagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class));
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);
        assertTrue(routesController.doAction(player));
        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
        verify(route).setClaimerPlayer(player.getIdentification());
        verify(player).notifyClaimedRoute(route);
    }

    @Test
    void testDoubleRoute() {
        Route doubleRoute = mock(Route.class);
        when(doubleRoute.getId()).thenReturn(2);
        wagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class));

        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);
        when(gameModel.getDoubleRouteOf(1)).thenReturn(doubleRoute);
        when(gameModel.deleteRoute(2)).thenReturn(true);

        assertTrue(routesController.doAction(player));

        verify(player).askClaimRoute();
        verify(player).removeWagonCards(wagonCards);
        verify(route).setClaimerPlayer(player.getIdentification());
        verify(gameModel).getDoubleRouteOf(1);
        verify(gameModel).deleteRoute(2);
    }


}