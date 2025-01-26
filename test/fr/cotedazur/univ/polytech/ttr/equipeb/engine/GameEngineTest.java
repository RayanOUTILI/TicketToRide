package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ScoreController;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    void setUp() {
        gameModel = mock(GameModel.class);
        gameView = mock(IGameViewable.class);
        controller = mock(Controller.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        scoreController = mock(ScoreController.class);
        players = List.of(player1, player2);
        gameEngine = new GameEngine(gameModel, players, gameView, Map.of(Action.PICK_WAGON_CARD, controller), scoreController);
    }


}