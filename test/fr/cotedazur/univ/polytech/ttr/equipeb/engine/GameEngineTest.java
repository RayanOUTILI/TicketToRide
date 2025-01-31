package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ScoreController;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.ScoreConsoleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    void testStartGame() throws JsonParseException, NoSuchFieldException, IllegalAccessException {
        List<Route> routes = (new MapFactory()).getMapFromJson();
        WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
        DestinationCardDeck destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getShortDestinationCards());

        PlayerFactory playerFactory = new PlayerFactory();

        Map<PlayerIdentification, IPlayerViewable> playersView = Map.of(
                PlayerIdentification.BLUE, mock(PlayerConsoleView.class),
                PlayerIdentification.RED, mock(PlayerConsoleView.class),
                PlayerIdentification.GREEN, mock(PlayerConsoleView.class)
        );

        List<PlayerModel> playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, playersView.get(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, playersView.get(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, playersView.get(PlayerIdentification.GREEN))
        );

        GameModel gameModel = new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes);
        List<Player> players = playerFactory.createThreeEasyBots(playerModels, gameModel);
        ScoreController scoreController = new ScoreController(gameModel, new ScoreConsoleView());

        this.gameEngine = new GameEngine(gameModel, players);

        Field gameViewField = GameEngine.class.getDeclaredField("gameView");
        gameViewField.setAccessible(true);
        gameViewField.set(gameEngine, this.gameView);

        Field scoreControllerField = GameEngine.class.getDeclaredField("scoreController");
        scoreControllerField.setAccessible(true);
        scoreControllerField.set(gameEngine, scoreController);

        assertTrue(gameEngine.initGame());
        assertTrue(gameEngine.initPlayers());
        int nbTurn = gameEngine.startGame();
        assertTrue(nbTurn >= 0);
    }


}