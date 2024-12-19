package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.EasyBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.Map;

public class GameEngine {
    private final VictoryController victoryController;
    private final ScoreController scoreController;
    private final GameModel gameModel;
    private final IGameViewable gameView;
    private final Map<Action, Controller> controllers;

    public GameEngine(GameModel gameModel) {
        this.gameModel = gameModel;

        this.victoryController = new VictoryController(gameModel);
        this.gameView = new GameConsoleView();
        this.controllers = Map.of(
            Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
            Action.CLAIM_ROUTE, new RoutesController(gameModel),
            Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel)
        );
        this.scoreController = new ScoreController(gameModel);
    }

    public void startGame(Player player) {

        controllers.values().forEach(controller -> controller.init(player));

        Victory victory;
        while((victory = victoryController.endGame()) == null) {
            handlePlayerAction(player);
            scoreController.updateScore(player);
            gameView.displayPlayerScore(player.getIdentification(), player.getScore());
        }
        gameView.displayEndGameReason(victory.reason());
    }

    private void handlePlayerAction(Player player) {
        Action action = player.askAction();

        if(action == null || !controllers.containsKey(action)) {
            player.actionRefused(action);
            return;
        }

        Controller controller = controllers.get(action);
        boolean success = controller.doAction(player);

        if (!success) player.actionRefused(action);
        else  player.actionCompleted(action);
    }
}
