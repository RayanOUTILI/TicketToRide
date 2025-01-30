package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutesControllerFerryTest {
    private RoutesController routesController;
    private IRoutesControllerGameModel gameModel;
    private Player player;
    private ClaimRoute claimRoute;
    private Route route;

    @BeforeEach
    void setup() {
        gameModel = mock(IRoutesControllerGameModel.class);
        when(gameModel.getNbOfPlayers()).thenReturn(3);
        when(gameModel.getRoute(1)).thenReturn(route);
        route = mock(Route.class);
        when(route.getId()).thenReturn(1);
        when(route.isClaimed()).thenReturn(false);
        when(route.getLength()).thenReturn(3);
        when(route.getNbLocomotives()).thenReturn(1);
        when(route.getType()).thenReturn(RouteType.FERRY);
        when(gameModel.getRoute(1)).thenReturn(route);
        claimRoute = mock(ClaimRoute.class);
        when(claimRoute.route()).thenReturn(route);
        routesController = new RoutesController(gameModel);
        player = mock(Player.class);
        when(player.getIdentification()).thenReturn(PlayerIdentification.RED);
        when(player.askClaimRoute()).thenReturn(claimRoute);
        when(player.getNumberOfWagons()).thenReturn(3);
    }

    @Test
    void testClaimRouteColorAnyWithColorBlue() {
        when(route.getColor()).thenReturn(Color.ANY);

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotive = mock(WagonCard.class);
        when(wagonCardLocomotive.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardBlue2, wagonCardLocomotive));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorBlueWithLocomotives() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotive2 = mock(WagonCard.class);
        when(wagonCardLocomotive2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLocomotive = mock(WagonCard.class);
        when(wagonCardLocomotive.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardLocomotive2, wagonCardLocomotive));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testFailedClaimRouteColorBlueWithNotEnoughLocomotives() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard wagonCardBlue3 = mock(WagonCard.class);
        when(wagonCardBlue3.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue1 = mock(WagonCard.class);
        when(wagonCardBlue1.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue3, wagonCardBlue1, wagonCardBlue2));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_FERRY_NOT_ENOUGH_LOCOMOTIVES, actionRefused.get());

        verify(player).replaceRemovedWagonCards(wagonCards);
        verify(player, times(0)).notifyClaimedRoute(route);
        verify(gameModel, times(0)).discardWagonCards(wagonCards);
        verify(route, times(0)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testFailedClaimRouteColorBlueWithWrongColor() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard wagonCardRed = mock(WagonCard.class);
        when(wagonCardRed.getColor()).thenReturn(Color.RED);
        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotives = mock(WagonCard.class);
        when(wagonCardLocomotives.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardRed, wagonCardBlue, wagonCardLocomotives));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_FERRY_WRONG_WAGON_CARDS_COLOR, actionRefused.get());

        verify(player).replaceRemovedWagonCards(wagonCards);
        verify(player, times(0)).notifyClaimedRoute(route);
        verify(gameModel, times(0)).discardWagonCards(wagonCards);
        verify(route, times(0)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testFailedClaimRouteColorBlueWithOnlyAnyColor() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard wagonCardLocomotive1 = mock(WagonCard.class);
        when(wagonCardLocomotive1.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLocomotive2 = mock(WagonCard.class);
        when(wagonCardLocomotive2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLocomotive3 = mock(WagonCard.class);
        when(wagonCardLocomotive3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardLocomotive1, wagonCardLocomotive2, wagonCardLocomotive3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);
        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }
}
