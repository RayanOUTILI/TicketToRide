package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.IPlayerActionsControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerTest {

    private Player player;
    private IPlayerActionsControllable actionsController;
    private IPlayerModelControllable modelController;

    @BeforeEach
    void setUp() {
        actionsController = mock(IPlayerActionsControllable.class);
        modelController = mock(IPlayerModelControllable.class);
        player = new Player(actionsController, modelController);
    }

    @Test
    void askActionReturnsAction() {
        Action action = Action.CLAIM_ROUTE;
        when(actionsController.askAction()).thenReturn(action);
        assertEquals(action, player.askAction());
    }

    @Test
    void askClaimRouteReturnsClaimRoute() {
        ClaimObject<RouteReadOnly> claimRoute = mock(ClaimObject.class);
        when(actionsController.askClaimRoute()).thenReturn(claimRoute);
        assertEquals(claimRoute, player.askClaimRoute());
    }

    @Test
    void askClaimStationReturnsClaimStation() {
        ClaimObject<CityReadOnly> claimStation = mock(ClaimObject.class);
        when(actionsController.askClaimStation()).thenReturn(claimStation);
        assertEquals(claimStation, player.askClaimStation());
    }

    @Test
    void askDestinationCardsReturnsCards() {
        List<DestinationCard> cards = Collections.singletonList(mock(DestinationCard.class));
        when(actionsController.askDestinationCards(cards)).thenReturn(cards);
        assertEquals(cards, player.askDestinationCards(cards));
    }

    @Test
    void actionRefusedCallsController() {
        player.actionRefused(Action.STOP, ReasonActionRefused.ACTION_INVALID);
        verify(actionsController).actionRefused(Action.STOP, ReasonActionRefused.ACTION_INVALID);
    }

    @Test
    void actionCompletedCallsController() {
        Action action = Action.CLAIM_ROUTE;
        player.actionCompleted(action);
        verify(actionsController).actionCompleted(action);
    }

    @Test
    void askWagonCardsForTunnelReturnsCards() {
        List<WagonCard> cards = Collections.singletonList(mock(WagonCard.class));
        when(actionsController.askWagonCardsForTunnel(3, Color.RED)).thenReturn(cards);
        assertEquals(cards, player.askWagonCardsForTunnel(3, Color.RED));
    }

    @Test
    void askDrawWagonCardReturnsAction() {
        List<ActionDrawWagonCard> actions = Collections.singletonList(ActionDrawWagonCard.DRAW_FROM_DECK);
        when(actionsController.askDrawWagonCard(actions)).thenReturn(Optional.of(actions.getFirst()));
        assertEquals(Optional.of(actions.getFirst()), player.askDrawWagonCard(actions));
    }

    @Test
    void askWagonCardFromShownCardsReturnsCard() {
        WagonCard card = mock(WagonCard.class);
        when(actionsController.askWagonCardFromShownCards()).thenReturn(card);
        assertEquals(card, player.askWagonCardFromShownCards());
    }

    @Test
    void askChooseRouteStationReturnsRoute() {
        CityReadOnly city = mock(CityReadOnly.class);
        RouteReadOnly route = mock(RouteReadOnly.class);
        when(actionsController.askChooseRouteStation(city)).thenReturn(route);
        assertEquals(route, player.askChooseRouteStation(city));
    }

    @Test
    void getIdentificationReturnsIdentification() {
        PlayerIdentification identification = PlayerIdentification.BLUE;
        when(modelController.getIdentification()).thenReturn(identification);
        assertEquals(identification, player.getIdentification());
    }

    @Test
    void receivedWagonCardCallsController() {
        WagonCard card = mock(WagonCard.class);
        player.receivedWagonCard(card);
        verify(modelController).receivedWagonCard(card);
    }

    @Test
    void receivedWagonCardsCallsController() {
        List<WagonCard> cards = Collections.singletonList(mock(WagonCard.class));
        player.receivedWagonCards(cards);
        verify(modelController).receivedWagonCards(cards);
    }

    @Test
    void removeWagonCardsCallsController() {
        List<WagonCard> cards = Collections.singletonList(mock(WagonCard.class));
        player.removeWagonCards(cards);
        verify(modelController).removeWagonCards(cards);
    }

    @Test
    void replaceRemovedWagonCardsCallsController() {
        List<WagonCard> cards = Collections.singletonList(mock(WagonCard.class));
        player.replaceRemovedWagonCards(cards);
        verify(modelController).replaceRemovedWagonCards(cards);
    }

    @Test
    void notifyClaimedRouteCallsController() {
        RouteReadOnly route = mock(RouteReadOnly.class);
        player.notifyClaimedRoute(route);
        verify(modelController).notifyClaimedRoute(route);
    }

    @Test
    void notifyClaimedStationCallsController() {
        CityReadOnly city = mock(CityReadOnly.class);
        List<WagonCard> cards = Collections.singletonList(mock(WagonCard.class));
        player.notifyClaimedStation(city, cards);
        verify(modelController).notifyClaimedStation(city, cards);
    }

    @Test
    void setNumberOfWagonsCallsController() {
        player.setNumberOfWagons(5);
        verify(modelController).setNumberOfWagons(5);
    }

    @Test
    void getNumberOfWagonsReturnsNumber() {
        when(modelController.getNumberOfWagons()).thenReturn(5);
        assertEquals(5, player.getNumberOfWagons());
    }

    @Test
    void removeWagonsCallsController() {
        player.removeWagons(3);
        verify(modelController).removeWagons(3);
    }

    @Test
    void addChosenRouteStationCallsController() {
        RouteReadOnly route = mock(RouteReadOnly.class);
        player.addChosenRouteStation(route);
        verify(modelController).addChosenRouteStation(route);
    }

    @Test
    void getSelectedStationRoutesReturnsRoutes() {
        List<RouteReadOnly> routes = Collections.singletonList(mock(RouteReadOnly.class));
        when(modelController.getSelectedStationRoutes()).thenReturn(routes);
        assertEquals(routes, player.getSelectedStationRoutes());
    }

    @Test
    void receiveDestinationCardsCallsController() {
        List<DestinationCard> cards = Collections.singletonList(mock(DestinationCard.class));
        player.receiveDestinationCards(cards);
        verify(modelController).receiveDestinationCards(cards);
    }

    @Test
    void getDestinationCardsHandReturnsCards() {
        List<DestinationCard> cards = Collections.singletonList(mock(DestinationCard.class));
        when(modelController.getDestinationCards()).thenReturn(cards);
        assertEquals(cards, player.getDestinationCards());
    }

    @Test
    void defineStartingStationsNumberCallsController() {
        player.defineStartingStationsNumber(3);
        verify(modelController).defineStartingStationsNumber(3);
    }

    @Test
    void getStationsLeftReturnsNumber() {
        when(modelController.getStationsLeft()).thenReturn(2);
        assertEquals(2, player.getStationsLeft());
    }

    @Test
    void decrementStationsLeftCallsController() {
        player.decrementStationsLeft();
        verify(modelController).decrementStationsLeft();
    }

    @Test
    void setScoreCallsController() {
        player.setScore(10);
        verify(modelController).setScore(10);
    }

    @Test
    void getScoreReturnsScore() {
        when(modelController.getScore()).thenReturn(10);
        assertEquals(10, player.getScore());
    }
}