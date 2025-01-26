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

public class RoutesControllerTunnelTest {
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
        when(route.getNbLocomotives()).thenReturn(0);
        when(route.getType()).thenReturn(RouteType.TUNNEL);
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
    void testClaimRouteColorAnyWithColorBlueEmptyDeck() {
        when(route.getColor()).thenReturn(Color.ANY);

        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(new ArrayList<>());

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue3 = mock(WagonCard.class);
        when(wagonCardBlue3.getColor()).thenReturn(Color.BLUE);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardBlue2, wagonCardBlue3));
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
    void testClaimRouteColorAnyWithColorBlueAndNoSupplements() {
        when(route.getColor()).thenReturn(Color.ANY);

        List<WagonCard> drawnWagonCards = List.of(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class));
        drawnWagonCards.forEach(m -> when(m.getColor()).thenReturn(Color.RED));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

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
    void testClaimRouteColorBlueWithColorBlueAnd1SupplementBlue() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class)));
        drawnWagonCards.forEach(m -> when(m.getColor()).thenReturn(Color.RED));
        drawnWagonCards.add(drawnWagonCardBlue);
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotive = mock(WagonCard.class);
        when(wagonCardLocomotive.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardBlue2, wagonCardLocomotive));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        WagonCard wagonCardSupplement = mock(WagonCard.class);
        when(wagonCardSupplement.getColor()).thenReturn(Color.BLUE);
        when(player.askWagonCardsForTunnel(1, Color.BLUE)).thenReturn(List.of(wagonCardSupplement));
        when(player.removeWagonCards(List.of(wagonCardSupplement))).thenReturn(List.of(wagonCardSupplement));

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorBlueWithColorBlueAnd3SupplementsBlue() {
        when(route.getColor()).thenReturn(Color.BLUE);

        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));
        drawnWagonCards.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotive = mock(WagonCard.class);
        when(wagonCardLocomotive.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardBlue2, wagonCardLocomotive));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(3, Color.BLUE)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(wagonCardsSupplements);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorBlueWithColorBlueAndSupplementsAnyAndBlue() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard drawnWagonCardAny = mock(WagonCard.class);
        when(drawnWagonCardAny.getColor()).thenReturn(Color.ANY);
        WagonCard drawnWagonCardBlack = mock(WagonCard.class);
        when(drawnWagonCardBlack.getColor()).thenReturn(Color.BLACK);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(drawnWagonCardBlue, drawnWagonCardAny, drawnWagonCardBlack));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardBlue = mock(WagonCard.class);
        when(wagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardBlue2 = mock(WagonCard.class);
        when(wagonCardBlue2.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLocomotive = mock(WagonCard.class);
        when(wagonCardLocomotive.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue, wagonCardBlue2, wagonCardLocomotive));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(1, Color.BLUE)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(wagonCardsSupplements);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorAnyWithColorAnyAndSupplementsAny() {
        when(route.getColor()).thenReturn(Color.ANY);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard drawnWagonCardAny = mock(WagonCard.class);
        when(drawnWagonCardAny.getColor()).thenReturn(Color.ANY);
        WagonCard drawnWagonCardBlack = mock(WagonCard.class);
        when(drawnWagonCardBlack.getColor()).thenReturn(Color.BLUE);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(drawnWagonCardBlue, drawnWagonCardAny, drawnWagonCardBlack));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardLoc1 = mock(WagonCard.class);
        when(wagonCardLoc1.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc2 = mock(WagonCard.class);
        when(wagonCardLoc2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc3 = mock(WagonCard.class);
        when(wagonCardLoc3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardLoc1, wagonCardLoc2, wagonCardLoc3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.ANY));
        when(player.askWagonCardsForTunnel(1, Color.ANY)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(wagonCardsSupplements);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isEmpty());

        verify(player, times(0)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(1)).notifyClaimedRoute(route);
        verify(gameModel, times(1)).discardWagonCards(wagonCards);
        verify(route, times(1)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorAnyWithColorAnyAndSupplementsBlue() {
        when(route.getColor()).thenReturn(Color.ANY);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard drawnWagonCardAny = mock(WagonCard.class);
        when(drawnWagonCardAny.getColor()).thenReturn(Color.ANY);
        WagonCard drawnWagonCardBlack = mock(WagonCard.class);
        when(drawnWagonCardBlack.getColor()).thenReturn(Color.BLUE);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(drawnWagonCardBlue, drawnWagonCardAny, drawnWagonCardBlack));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardLoc1 = mock(WagonCard.class);
        when(wagonCardLoc1.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc2 = mock(WagonCard.class);
        when(wagonCardLoc2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc3 = mock(WagonCard.class);
        when(wagonCardLoc3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardLoc1, wagonCardLoc2, wagonCardLoc3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(1, Color.ANY)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(wagonCardsSupplements);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_REMOVED_WAGON_CARDS, actionRefused.get());

        verify(player, times(1)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(0)).notifyClaimedRoute(route);
        verify(gameModel).discardWagonCards(anyList());
        verify(route, times(0)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorAnyWithColorBlueAndSupplementsAny() {
        when(route.getColor()).thenReturn(Color.ANY);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard drawnWagonCardAny = mock(WagonCard.class);
        when(drawnWagonCardAny.getColor()).thenReturn(Color.ANY);
        WagonCard drawnWagonCardBlack = mock(WagonCard.class);
        when(drawnWagonCardBlack.getColor()).thenReturn(Color.BLUE);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(drawnWagonCardBlue, drawnWagonCardAny, drawnWagonCardBlack));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardBlue1 = mock(WagonCard.class);
        when(wagonCardBlue1.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLoc2 = mock(WagonCard.class);
        when(wagonCardLoc2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc3 = mock(WagonCard.class);
        when(wagonCardLoc3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue1, wagonCardLoc2, wagonCardLoc3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(2, Color.BLUE)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(wagonCardsSupplements);

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isEmpty());
    }

    @Test
    void testClaimRouteColorAnyWrongRemoveCardSize() {
        when(route.getColor()).thenReturn(Color.ANY);

        WagonCard drawnWagonCardBlue = mock(WagonCard.class);
        when(drawnWagonCardBlue.getColor()).thenReturn(Color.BLUE);
        WagonCard drawnWagonCardAny = mock(WagonCard.class);
        when(drawnWagonCardAny.getColor()).thenReturn(Color.ANY);
        WagonCard drawnWagonCardBlack = mock(WagonCard.class);
        when(drawnWagonCardBlack.getColor()).thenReturn(Color.BLUE);
        List<WagonCard> drawnWagonCards = new ArrayList<>(List.of(drawnWagonCardBlue, drawnWagonCardAny, drawnWagonCardBlack));
        when(gameModel.drawCardsFromWagonCardDeck(3)).thenReturn(drawnWagonCards);

        WagonCard wagonCardBlue1 = mock(WagonCard.class);
        when(wagonCardBlue1.getColor()).thenReturn(Color.BLUE);
        WagonCard wagonCardLoc2 = mock(WagonCard.class);
        when(wagonCardLoc2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc3 = mock(WagonCard.class);
        when(wagonCardLoc3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardBlue1, wagonCardLoc2, wagonCardLoc3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(2, Color.BLUE)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(new ArrayList<>());

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isPresent());

        assertEquals(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_WAGON_CARDS, actionRefused.get());

        verify(player, times(1)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(0)).notifyClaimedRoute(route);
        verify(gameModel).discardWagonCards(anyList());
        verify(route, times(0)).setClaimerPlayer(player.getIdentification());
    }

    @Test
    void testClaimRouteColorBlueWrongColor() {
        when(route.getColor()).thenReturn(Color.BLUE);

        WagonCard wagonCardRed = mock(WagonCard.class);
        when(wagonCardRed.getColor()).thenReturn(Color.RED);
        WagonCard wagonCardLoc2 = mock(WagonCard.class);
        when(wagonCardLoc2.getColor()).thenReturn(Color.ANY);
        WagonCard wagonCardLoc3 = mock(WagonCard.class);
        when(wagonCardLoc3.getColor()).thenReturn(Color.ANY);

        List<WagonCard> wagonCards = new ArrayList<>(List.of(wagonCardRed, wagonCardLoc2, wagonCardLoc3));
        when(claimRoute.wagonCards()).thenReturn(wagonCards);
        when(player.removeWagonCards(wagonCards)).thenReturn(wagonCards);

        ArrayList<WagonCard> wagonCardsSupplements = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class)));
        wagonCardsSupplements.forEach(m -> when(m.getColor()).thenReturn(Color.BLUE));
        when(player.askWagonCardsForTunnel(2, Color.BLUE)).thenReturn(wagonCardsSupplements);
        when(player.removeWagonCards(wagonCardsSupplements)).thenReturn(new ArrayList<>());

        Optional<ReasonActionRefused> actionRefused = routesController.doAction(player);

        assertTrue(actionRefused.isPresent());

        assertEquals(ReasonActionRefused.ROUTE_TUNNEL_WRONG_WAGON_CARDS_COLOR, actionRefused.get());

        verify(player, times(1)).replaceRemovedWagonCards(wagonCards);
        verify(player, times(0)).notifyClaimedRoute(route);
        verify(route, times(0)).setClaimerPlayer(player.getIdentification());
    }



}

