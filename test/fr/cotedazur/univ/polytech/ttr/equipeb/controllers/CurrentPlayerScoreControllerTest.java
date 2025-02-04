package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.ICurrentPlayerScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrentPlayerScoreControllerTest {
    private static final PlayerIdentification PLAYER_IDENTIFICATION = PlayerIdentification.BLACK;

    private CurrentPlayerScoreController currentPlayerScoreController;
    private ICurrentPlayerScoreControllerGameModel gameModel;
    private Player player;
    private RouteReadOnly route;
    private List<RouteReadOnly> claimedRoutes;

    @BeforeEach
    void setUp() {
        gameModel = mock(ICurrentPlayerScoreControllerGameModel.class);
        currentPlayerScoreController = new CurrentPlayerScoreController(gameModel);
        player = new Player(null, new PlayerModel(PLAYER_IDENTIFICATION, null));
        route = mock(RouteReadOnly.class);
        claimedRoutes = new ArrayList<>(List.of(route));
        when(gameModel.getAllRoutesClaimedByPlayer(PLAYER_IDENTIFICATION)).thenReturn(claimedRoutes);
        assertTrue(currentPlayerScoreController.initGame());
        assertTrue(currentPlayerScoreController.initPlayer(player));
    }

    @AfterEach
    void tearDown() {
        assertTrue(currentPlayerScoreController.resetPlayer(player));
        assertTrue(currentPlayerScoreController.resetGame());
    }

    @Test
    void testRouteLength3() {
        route = mock(RouteReadOnly.class);
        when(gameModel.getAllRoutesClaimedByPlayer(PLAYER_IDENTIFICATION)).thenReturn(List.of(route));
        when(route.getLength()).thenReturn(3);

        currentPlayerScoreController.doAction(player);

        assertEquals(4, player.getScore());
    }

    @Test
    void testRouteNotSupportedLength() {
        when(route.getLength()).thenReturn(5);

        currentPlayerScoreController.doAction(player);

        assertEquals(0, player.getScore());
    }

    @Test
    void testCombo() {
        RouteReadOnly length1 = mock(RouteReadOnly.class);
        RouteReadOnly length2 = mock(RouteReadOnly.class);
        RouteReadOnly length4 = mock(RouteReadOnly.class);
        RouteReadOnly length6 = mock(RouteReadOnly.class);
        RouteReadOnly length8 = mock(RouteReadOnly.class);
        when(length1.getLength()).thenReturn(1);
        when(length2.getLength()).thenReturn(2);
        when(length4.getLength()).thenReturn(4);
        when(length6.getLength()).thenReturn(6);
        when(length8.getLength()).thenReturn(8);

        this.claimedRoutes.clear();
        this.claimedRoutes.addAll(List.of(length1, length2, length4, length6, length8));

        currentPlayerScoreController.doAction(player);

        assertEquals(46, player.getScore());
    }
}