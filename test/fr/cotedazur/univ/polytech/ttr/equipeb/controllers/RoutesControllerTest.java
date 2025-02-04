package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoutesControllerTest {
    RoutesController routesController;
    IRoutesControllerGameModel gameModel;
    Player player;

    @BeforeEach
    void setUp() {
        gameModel = mock(IRoutesControllerGameModel.class);
        routesController = new RoutesController(gameModel);
        player = mock(Player.class);
    }

    @Test
    void testInitGame() {
        when(gameModel.setAllRoutesIDs()).thenReturn(true);
        assertTrue(routesController.initGame());

        when(gameModel.setAllRoutesIDs()).thenReturn(false);
        assertFalse(routesController.initGame());
    }

    @Test
    void testInitPlayer() {
        when(player.setNumberOfWagons(45)).thenReturn(true);
        assertTrue(routesController.initPlayer(player));

        when(player.setNumberOfWagons(45)).thenReturn(false);
        assertFalse(routesController.initPlayer(player));
    }

    @Test
    void testResetPlayer() {
        when(player.clearNumberOfWagons()).thenReturn(true);
        assertTrue(routesController.resetPlayer(player));

        when(player.clearNumberOfWagons()).thenReturn(false);
        assertFalse(routesController.resetPlayer(player));
    }

    @Test
    void testResetGame() {
        when(gameModel.retrieveDeletedRoutes()).thenReturn(true);
        when(gameModel.setAllRoutesNotClaimed()).thenReturn(true);
        assertTrue(routesController.resetGame());

        when(gameModel.retrieveDeletedRoutes()).thenReturn(false);
        when(gameModel.setAllRoutesNotClaimed()).thenReturn(true);
        assertFalse(routesController.resetGame());

        when(gameModel.retrieveDeletedRoutes()).thenReturn(true);
        when(gameModel.setAllRoutesNotClaimed()).thenReturn(false);
        assertFalse(routesController.resetGame());

        when(gameModel.retrieveDeletedRoutes()).thenReturn(false);
        when(gameModel.setAllRoutesNotClaimed()).thenReturn(false);
        assertFalse(routesController.resetGame());
    }
}
