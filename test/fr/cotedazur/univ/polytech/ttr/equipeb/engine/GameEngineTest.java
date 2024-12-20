package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ScoreController;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.VictoryController;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class GameEngineTest {

    private GameModel gameModel;
    private VictoryController victoryController;
    private IGameViewable gameView;
    private Controller controller;
    private GameEngine gameEngine;
    private Player player1;
    private Player player2;
    private List<Player> players;

    private ScoreController scoreController;

    @BeforeEach
    void setUp() {
        gameModel = mock(GameModel.class);
        victoryController = mock(VictoryController.class);
        gameView = mock(IGameViewable.class);
        controller = mock(Controller.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        scoreController = mock(ScoreController.class);
        players = List.of(player1, player2);
        gameEngine = new GameEngine(gameModel, players, victoryController, gameView, Map.of(Action.PICK_WAGON_CARD, controller), scoreController);
    }

    @Test
    void testEndGame() {
        when(victoryController.endGame()).thenReturn(null, EndGameReasons.EMPTY_WAGON_CARDS_DECK);
        when(controller.init(any())).thenReturn(true);
        when(controller.doAction(any())).thenReturn(true);
        players.forEach(player -> when(player.askAction()).thenReturn(Action.PICK_WAGON_CARD));

        assertDoesNotThrow(() -> gameEngine.startGame());

        verify(victoryController, times(1)).endTurn();
        verify(victoryController, times(2)).endGame();
    }

    @Test
    void testWrongAction() {
        when(player1.askAction()).thenReturn(null, Action.PICK_WAGON_CARD);
        when(player2.askAction()).thenReturn(Action.PICK_WAGON_CARD);
        when(victoryController.endGame()).thenReturn(null, null, EndGameReasons.EMPTY_WAGON_CARDS_DECK);
        when(controller.init(any())).thenReturn(true);
        when(controller.doAction(any())).thenReturn(true);

        assertDoesNotThrow(() -> gameEngine.startGame());

        verify(player1, times(2)).askAction();
        verify(player2, times(1)).askAction();
        verify(victoryController, times(1)).endTurn();
        verify(victoryController, times(3)).endGame();
    }
}