package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.EndGameScoreController;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameEngineTest {

    private GameModel gameModel;
    private IGameViewable gameView;
    private Controller controller;
    private GameEngine gameEngine;
    private Player player1;
    private Player player2;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        gameModel = mock(GameModel.class);
        gameView = mock(IGameViewable.class);
        controller = mock(Controller.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        players = List.of(player1, player2);

        List<Controller> endGameControllers = List.of(
                mock(EndGameScoreController.class)
        );

        gameEngine = new GameEngine(gameModel, Map.of(Action.PICK_WAGON_CARD, controller), new ArrayList<>(), endGameControllers, players, gameView);

    }

    @Test
    void testInitGame() {
        when(controller.initGame()).thenReturn(true);

        assertTrue(gameEngine.initGame());
        verify(controller, times(1)).initGame();
    }

    @Test
    void testInitPlayer() {
        when(controller.initPlayer(player1)).thenReturn(true);
        when(controller.initPlayer(player2)).thenReturn(true);

        assertTrue(gameEngine.initPlayers());
        verify(controller, times(1)).initPlayer(player1);
        verify(controller, times(1)).initPlayer(player2);
    }

    @Test
    void testStop() {
        when(controller.initGame()).thenReturn(true);
        when(controller.initPlayer(player1)).thenReturn(true);
        when(controller.initPlayer(player2)).thenReturn(true);

        when(player1.getIdentification()).thenReturn(PlayerIdentification.BLUE);
        when(player2.getIdentification()).thenReturn(PlayerIdentification.RED);

        assertTrue(gameEngine.initGame());
        assertTrue(gameEngine.initPlayers());

        when(player1.askAction()).thenReturn(Action.STOP);
        when(player2.askAction()).thenReturn(Action.STOP);

        int nbTurn = gameEngine.startGame();
        assertEquals(1, nbTurn);
    }

    @Test
    void testStop2Turn() {
        when(controller.initGame()).thenReturn(true);
        when(controller.initPlayer(player1)).thenReturn(true);
        when(controller.initPlayer(player2)).thenReturn(true);

        when(player1.getIdentification()).thenReturn(PlayerIdentification.BLUE);
        when(player2.getIdentification()).thenReturn(PlayerIdentification.RED);

        assertTrue(gameEngine.initGame());
        assertTrue(gameEngine.initPlayers());

        when(player1.askAction()).thenReturn(Action.STOP, Action.PICK_DESTINATION_CARDS, Action.STOP);
        when(player2.askAction()).thenReturn(Action.PICK_WAGON_CARD, Action.STOP, Action.STOP);

        int nbTurn = gameEngine.startGame();
        assertEquals(2, nbTurn);

        verify(player1, times(3)).askAction();
        verify(player2, times(2)).askAction();
    }

    @Test
    void testActionRefused() {
        when(controller.initGame()).thenReturn(true);
        when(controller.initPlayer(player1)).thenReturn(true);
        when(controller.initPlayer(player2)).thenReturn(true);
        when(controller.doAction(player1)).thenReturn(Optional.of(ReasonActionRefused.ACTION_INVALID));

        when(player1.getIdentification()).thenReturn(PlayerIdentification.BLUE);
        when(player2.getIdentification()).thenReturn(PlayerIdentification.RED);

        assertTrue(gameEngine.initGame());
        assertTrue(gameEngine.initPlayers());

        when(player1.askAction()).thenReturn(Action.PICK_WAGON_CARD, Action.STOP, Action.STOP);
        when(player2.askAction()).thenReturn(Action.PICK_WAGON_CARD, Action.PICK_WAGON_CARD, Action.STOP);

        int nbTurn = gameEngine.startGame();
        assertEquals(3, nbTurn);

        verify(player1, times(4)).askAction();
        verify(player2, times(3)).askAction();
        verify(controller, times(2)).doAction(player2);
    }
}