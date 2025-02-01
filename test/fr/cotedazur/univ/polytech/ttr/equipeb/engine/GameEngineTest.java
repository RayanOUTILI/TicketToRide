package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ScoreController;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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

    private ScoreController scoreController;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        gameModel = mock(GameModel.class);
        gameView = mock(IGameViewable.class);
        controller = mock(Controller.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        scoreController = mock(ScoreController.class);
        players = List.of(player1, player2);
        gameEngine = new GameEngine(gameModel, players);

        Field gameViewField = GameEngine.class.getDeclaredField("gameView");
        gameViewField.setAccessible(true);
        gameViewField.set(gameEngine, gameView);

        Field scoreControllerField = GameEngine.class.getDeclaredField("scoreController");
        scoreControllerField.setAccessible(true);
        scoreControllerField.set(gameEngine, scoreController);

        Field controllerField = GameEngine.class.getDeclaredField("gameControllers");
        controllerField.setAccessible(true);
        controllerField.set(gameEngine, Map.of(Action.PICK_WAGON_CARD, controller));
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
        verify(scoreController, times(1)).calculatePlacedRoutesScore(player2);
        verify(scoreController, times(0)).calculatePlacedRoutesScore(player1);
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