package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.*;

public class GameEngine {
    private final GameModel gameModel;
    private final Optional<IGameViewable> gameView;
    private final Map<Action, Controller> gameControllers;
    private final List<Controller> endPlayerTurnControllers;
    private final List<Controller> endGameControllers;
    private final List<Player> players;
    private Iterator<Player> playerIterator;
    private Optional<PlayerIdentification> lastTurnPlayer;

    private Player currentPlayer;

    public GameEngine(
            GameModel gameModel,
            Map<Action, Controller> gameControllers,
            List<Controller> endPlayerTurnControllers,
            List<Controller> endGameControllers,
            List<Player> players,
            IGameViewable view
    ) {
        this.gameModel = gameModel;
        this.players = players;

        this.gameControllers = gameControllers;
        this.endPlayerTurnControllers = endPlayerTurnControllers;
        this.endGameControllers = endGameControllers;

        this.gameView = Optional.ofNullable(view);
        this.lastTurnPlayer = Optional.empty();
    }

    public GameEngine(GameModel gameModel, Map<Action, Controller> gameControllers, List<Controller> endPlayerTurnControllers, List<Controller> endGameControllers, List<Player> players) {
        this(gameModel, gameControllers, endPlayerTurnControllers, endGameControllers, players, null);
    }

    private List<Controller> getAllControllers() {
        List<Controller> controllers = new ArrayList<>(gameControllers.values());
        controllers.addAll(endPlayerTurnControllers);
        controllers.addAll(endGameControllers);
        return controllers;
    }

    public boolean initGame() {
        boolean success;

        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();

        Set<Map.Entry<Action, Controller>> entries = gameControllers.entrySet();
        Iterator<Map.Entry<Action, Controller>> iterator = entries.iterator();
        for(success = true; success && iterator.hasNext();) {
            Map.Entry<Action, Controller> entry = iterator.next();
            success = entry.getValue().initGame();
        }

        return success;
    }

    public boolean initPlayers() {
        boolean success;

        for(Controller controller : getAllControllers()) {
            for (Player player : players) {
                success = controller.initPlayer(player);
                if (!success){
                    return false;
                }
            }
        }

        return true;
    }

    public int startGame() {
        if (gameView.isPresent()) this.gameView.get().displayNewGame();
        int nbTurn = 1;
        int previousNbTurn = 0;

        boolean forcedEndGame = false;
        int nbPlayersWantStop = 0;

        while(!isWasTheLastTurn() && !forcedEndGame) {
            if (previousNbTurn != nbTurn) {
                previousNbTurn = nbTurn;
                if (gameView.isPresent()) this.gameView.get().displayNewTurn(nbTurn);
            }
            TypeActionHandled actionHandled ;

            if(isHisLastTurn(currentPlayer)) lastTurnPlayer = Optional.of(currentPlayer.getIdentification());

            do {
                actionHandled = handlePlayerAction(currentPlayer);
            } while (actionHandled == TypeActionHandled.REFUSED);

            if(actionHandled == TypeActionHandled.STOP) {
                nbPlayersWantStop++;
                if(nbPlayersWantStop == players.size()) {
                    forcedEndGame = true;
                }
            }
            else {
                nbPlayersWantStop = 0;
            }

            this.endPlayerTurnControllers.forEach(controller -> controller.doAction(currentPlayer));

            boolean newTurn = nextPlayer();

            if (!newTurn){
                if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();

                nbTurn++;
            }
        }

        int finalNbTurn = nbTurn;
        lastTurnPlayer.ifPresent(playerIdentification ->
                gameView.ifPresent(v -> v.displayEndGameReason(playerIdentification, currentPlayer.getNumberOfWagons(), finalNbTurn))
        );

        askForEndGameActions();

        PlayerModel winner = gameModel.getWinner();

        if(winner != null && gameView.isPresent()) gameView.get().displayWinner(winner.getIdentification(), winner.getScore());

        return nbTurn;
    }

    public boolean reset() {
        lastTurnPlayer = Optional.empty();
        boolean success = true;
        for(Controller controller : getAllControllers()) {
            for (Player player : players) {
                success = controller.resetPlayer(player);
                if (!success) {
                    return false;
                }
            }
            success = controller.resetGame();
            if(!success) {
                return false;
            }
        }
        return success;
    }

    private void askForEndGameActions() {
        for(Controller controller : endGameControllers) {

            for (Player player : players) {
                Optional<ReasonActionRefused> endGameAction;
                do {
                    endGameAction = controller.doAction(player);
                } while (endGameAction.isPresent());
            }
        }
    }

    private boolean isWasTheLastTurn() {
        return lastTurnPlayer.isPresent() && currentPlayer.getIdentification() == lastTurnPlayer.get();
    }

    private TypeActionHandled handlePlayerAction(Player player) {
        Action action = player.askAction();
        if(action == Action.STOP){
            player.actionStop();
            return TypeActionHandled.STOP;
        }

        else if(action == null || !gameControllers.containsKey(action)) {
            player.actionRefused(action, ReasonActionRefused.ACTION_INVALID);
            return TypeActionHandled.REFUSED;
        }

        Controller controller = gameControllers.get(action);
        Optional<ReasonActionRefused> actionRefused = controller.doAction(player);

        TypeActionHandled typeActionHandled;

        if (actionRefused.isPresent() && !actionRefused.get().isActionSkipTurn()) {
            player.actionRefused(action, actionRefused.get());
            typeActionHandled = TypeActionHandled.REFUSED;
        }
        else if(actionRefused.isPresent()) {
            player.actionSkipped(action, actionRefused.get());
            typeActionHandled = TypeActionHandled.SKIPPED;
        }
        else {
            player.actionCompleted(action);
            typeActionHandled = TypeActionHandled.SUCCESS;
        }

        return typeActionHandled;
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

    private boolean isHisLastTurn(Player player) {
        return player.getNumberOfWagons() <= 2;
    }
}
