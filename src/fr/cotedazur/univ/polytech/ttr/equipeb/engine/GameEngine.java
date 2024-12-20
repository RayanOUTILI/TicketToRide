package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameEngine {
    private final VictoryController victoryController;
    private final ScoreController scoreController;
    private final GameModel gameModel;
    private final IGameViewable gameView;
    private final Map<Action, Controller> controllers;
    private final List<Player> players;
    private Iterator<Player> playerIterator;

    private Player currentPlayer;
    public GameEngine(GameModel gameModel, List<Player> players) {
        this.gameModel = gameModel;
        this.players = players;

        this.victoryController = new VictoryController(gameModel);
        this.gameView = new GameConsoleView();
        this.controllers = Map.of(
            Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
            Action.CLAIM_ROUTE, new RoutesController(gameModel),
            Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel)
        );
        this.scoreController = new ScoreController(gameModel);
        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();
    }

    protected GameEngine(GameModel gameModel, List<Player> players, VictoryController victoryController, IGameViewable gameView, Map<Action, Controller> controllers, ScoreController scoreController) {
        this.gameModel = gameModel;
        this.players = players;
        this.victoryController = victoryController;
        this.gameView = gameView;
        this.controllers = controllers;

        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();
        this.scoreController = new ScoreController(gameModel);

    }

    public int startGame() {
        int nbTurn = 0;

        players.forEach(player -> controllers.values().forEach(controller -> controller.init(player)));

        Victory victory;
        while((victory = victoryController.endGame()) == null) {
            boolean success;
            do {
                success = handlePlayerAction(currentPlayer);
            } while (!success);
            boolean newTurn = nextPlayer();
            if (newTurn){
    
            scoreController.updateScore(currentPlayer);
            gameView.displayPlayerScore(currentPlayer.getIdentification(), currentPlayer.getScore());
            victoryController.endTurn();
                nbTurn++;
            }
        }
        scoreController.calculateFinalScores();
        gameView.displayEndGameReason(victory.reason());

        gameModel.getPlayers().forEach(
                player -> gameView.displayPlayerScore(player.getIdentification(), player.getScore())
        );

        PlayerIdentification player = victory.getPlayerIdentification();
        PlayerModel winner = gameModel.getPlayer(player);

        if (winner != null) gameView.displayWinner(player, winner.getScore());

        return nbTurn;
    }

    protected boolean handlePlayerAction(Player player) {
        Action action = player.askAction();

        if(action == null || !controllers.containsKey(action)) {
            player.actionRefused(action);
            return false;
        }

        Controller controller = controllers.get(action);
        boolean success = controller.doAction(player);

        if (!success) player.actionRefused(action);
        else  player.actionCompleted(action);
        return success;
    }

    private boolean nextPlayer() {
        if (!playerIterator.hasNext()){
            playerIterator = players.iterator();
            currentPlayer = playerIterator.next();
            return false;
        }
        currentPlayer = playerIterator.next();
        return true;
    }
}
