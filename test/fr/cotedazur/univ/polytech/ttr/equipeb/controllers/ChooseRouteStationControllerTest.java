package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IChooseRouteStationControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChooseRouteStationControllerTest {

    private IChooseRouteStationControllerGameModel gameModel;
    private ChooseRouteStationController controller;
    private Player player;

    @BeforeEach
    void setUp() {
        gameModel = mock(IChooseRouteStationControllerGameModel.class);
        controller = new ChooseRouteStationController(gameModel);
        player = mock(Player.class);
    }

    @Test
    void testInitGame() {
        assertTrue(controller.initGame());
    }

    @Test
    void testInitPlayer() {
        assertTrue(controller.initPlayer(player));
    }

    @Test
    void testDoActionSuccess() {
        City marseille = new City("Marseille");
        City nice = new City("Nice");
        City paris = new City("Paris");
        Route marseilleNice = new Route(marseille, nice, 1, RouteType.TRAIN, Color.BLUE, 0);
        Route marseilleParis = new Route(marseille, paris, 1, RouteType.TRAIN, Color.RED, 0);

        when(player.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        when(gameModel.getCitiesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(Arrays.asList(marseille, nice));
        when(player.askChooseRouteStation(nice)).thenReturn(marseilleNice);
        when(player.askChooseRouteStation(marseille)).thenReturn(marseilleParis);
        when(gameModel.getAdjacentRoutes(nice)).thenReturn(Collections.singletonList(marseilleNice));
        when(gameModel.getAdjacentRoutes(marseille)).thenReturn(Arrays.asList(marseilleNice, marseilleParis));

        Optional<ReasonActionRefused> result = controller.doAction(player);

        assertFalse(result.isPresent());
        verify(player).addChosenRouteStation(marseilleNice);
        verify(player).addChosenRouteStation(marseilleParis);
    }

    @Test
    void testDoActionRouteInvalid() {
        City marseille = new City("Marseille");

        when(player.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        when(gameModel.getCitiesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(Collections.singletonList(marseille));
        when(player.askChooseRouteStation(marseille)).thenReturn(null);

        Optional<ReasonActionRefused> result = controller.doAction(player);

        assertTrue(result.isPresent());
        assertEquals(ReasonActionRefused.ROUTE_INVALID, result.get());
    }

    @Test
    void testDoActionChosenRouteNotConnectedToStation() {
        City marseille = new City("Marseille");
        City nice = new City("Nice");
        Route marseilleNice = new Route(marseille, nice, 1, RouteType.TRAIN, Color.BLUE, 0);

        when(player.getIdentification()).thenReturn(PlayerIdentification.BLACK);
        when(gameModel.getCitiesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(Collections.singletonList(marseille));
        when(player.askChooseRouteStation(marseille)).thenReturn(marseilleNice);
        when(gameModel.getAdjacentRoutes(marseille)).thenReturn(Collections.emptyList());

        Optional<ReasonActionRefused> result = controller.doAction(player);

        assertTrue(result.isPresent());
        assertEquals(ReasonActionRefused.CHOSEN_ROUTE_NOT_CONNECTED_TO_STATION, result.get());
    }

}