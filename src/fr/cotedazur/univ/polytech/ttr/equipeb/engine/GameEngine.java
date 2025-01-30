package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.*;

public class GameEngine {
    private final ScoreController scoreController;
    private final GameModel gameModel;
    private final IGameViewable gameView;
    private final Map<Action, Controller> controllers;
    private final List<Player> players;
    private Iterator<Player> playerIterator;
    private Optional<PlayerIdentification> lastTurnPlayer;

    private Player currentPlayer;
    public GameEngine(GameModel gameModel, List<Player> players) {
        this.gameModel = gameModel;
        this.players = players;

        this.gameView = new GameConsoleView();
        this.controllers = Map.of(
            Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
            Action.CLAIM_ROUTE, new RoutesController(gameModel),
            Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel),
            Action.PLACE_STATION, new StationController(gameModel)
        );
        this.scoreController = new ScoreController(gameModel);
        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();

        this.lastTurnPlayer = Optional.empty();
    }

    protected GameEngine(GameModel gameModel, List<Player> players, IGameViewable gameView, Map<Action, Controller> controllers, ScoreController scoreController) {
        this.gameModel = gameModel;
        this.players = players;
        this.gameView = gameView;
        this.controllers = controllers;

        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();
        this.scoreController = scoreController;

        this.lastTurnPlayer = Optional.empty();

    }

    public boolean initGame() {
        boolean success;

        Set<Map.Entry<Action, Controller>> entries = controllers.entrySet();
        Iterator<Map.Entry<Action, Controller>> iterator = entries.iterator();
        for(success = true; success && iterator.hasNext();) {
            Map.Entry<Action, Controller> entry = iterator.next();
            success = entry.getValue().initGame();
        }

        return success;
    }

    public boolean initPlayers() {
        boolean success = true;

        Iterator<Player> playersIterator = this.players.iterator();
        while(playersIterator.hasNext() && success) {
            Player player = playersIterator.next();

            Iterator<Map.Entry<Action, Controller>> entries = controllers.entrySet().iterator();
            while (entries.hasNext() && success) {
                Map.Entry<Action, Controller> entry = entries.next();
                success = entry.getValue().initPlayer(player);
            }
        }

        return success;
    }

    public int startGame() {
        int nbTurn = 0;

        while(lastTurnPlayer.isEmpty() || currentPlayer.getIdentification() != lastTurnPlayer.get()) {
            boolean success;

            if(lastTurn(currentPlayer)) lastTurnPlayer = Optional.of(currentPlayer.getIdentification());

            do {
                success = handlePlayerAction(currentPlayer);
            } while (!success);

            boolean newTurn = nextPlayer();

            if (newTurn){
                scoreController.updateScore(currentPlayer);

                if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();

                nbTurn++;
            }
        }
        scoreController.calculateFinalScores();

        gameView.displayEndGameReason(lastTurnPlayer.get(), currentPlayer.getNumberOfWagons());

        PlayerModel winner = gameModel.getWinner();
        if(winner != null) gameView.displayWinner(winner.getIdentification(), winner.getScore());

        return nbTurn;
    }

    protected boolean handlePlayerAction(Player player) {
        Action action = player.askAction();

        if(action == null || !controllers.containsKey(action)) {
            player.actionRefused(action, ReasonActionRefused.ACTION_INVALID);
            return false;
        }

        Controller controller = controllers.get(action);
        Optional<ReasonActionRefused> actionRefused = controller.doAction(player);

        if (actionRefused.isPresent()) {
            player.actionRefused(action, actionRefused.get());
        }
        else  player.actionCompleted(action);
        return actionRefused.isEmpty();
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

    private boolean lastTurn(Player player) {
        return player.getNumberOfWagons() <= 2;
    }
}
