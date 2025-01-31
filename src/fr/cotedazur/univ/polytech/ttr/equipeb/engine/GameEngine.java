package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
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

        boolean forcedEndGame = false;

        while(!isWasTheLastTurn() && !forcedEndGame) {
            boolean success = false;
            int failedAction;

            if(isHisLastTurn(currentPlayer)) lastTurnPlayer = Optional.of(currentPlayer.getIdentification());

            for(failedAction = 0; !success && failedAction < 3; failedAction++) {
                success = handlePlayerAction(currentPlayer);
            }

            if(!success) {
                forcedEndGame = isForcedEndGame();
            }

            boolean newTurn = nextPlayer();

            if (newTurn){
                scoreController.updateScore(currentPlayer);

                if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();

                nbTurn++;
            }
        }
        scoreController.calculateFinalScores();

        lastTurnPlayer.ifPresent(playerIdentification -> gameView.displayEndGameReason(playerIdentification, currentPlayer.getNumberOfWagons()));

        PlayerModel winner = gameModel.getWinner();

        if(winner != null) gameView.displayWinner(winner.getIdentification(), winner.getScore());

        return nbTurn;
    }

    private boolean isWasTheLastTurn() {
        return lastTurnPlayer.isPresent() && currentPlayer.getIdentification() == lastTurnPlayer.get();
    }

    private boolean handlePlayerAction(Player player) {
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

    private boolean isHisLastTurn(Player player) {
        return player.getNumberOfWagons() <= 2;
    }

    private boolean isForcedEndGame() {
        if(!gameModel.isWagonCardDeckEmpty() || !gameModel.isDestinationCardDeckEmpty()) return false;

        boolean canClaimRouteOrStation = false;

        Iterator<Player> playersIterator = players.iterator();
        while(playersIterator.hasNext() && !canClaimRouteOrStation) {
            Player player = playersIterator.next();
            canClaimRouteOrStation = canClaimRoute(player) || canClaimStation(player);
        }

        return !canClaimRouteOrStation;
    }

    private boolean canClaimRoute(Player player) {
        PlayerModel playerModel = gameModel.getPlayer(player.getIdentification());
        if(gameModel.getPlayer(player.getIdentification()).getNumberOfWagonCards() == 0) return false;

        Iterator<RouteReadOnly> routeIterator = gameModel.getNonControllableAvailableRoutes().iterator();
        boolean canClaim = false;

        while(routeIterator.hasNext() && !canClaim) {
            RouteReadOnly route = routeIterator.next();
            canClaim = !route.isClaimed() && playerModel.getNumberOfWagons() >= route.getLength() && playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), route.getType() == RouteType.FERRY ? route.getNbLocomotives() : 0).size() == route.getLength();
        }

        return canClaim;

    }

    private boolean canClaimStation(Player player) {
        PlayerModel playerModel = gameModel.getPlayer(player.getIdentification());
        if(player.getStationsLeft() == 0) return false;

        boolean canClaim = false;

        Iterator<City> cityIterator = gameModel.getAllCities().iterator();
        while(cityIterator.hasNext() && !canClaim) {
            City city = cityIterator.next();
            canClaim = !city.isClaimed() && playerModel.getWagonCardsIncludingAnyColor(3 - (player.getStationsLeft() - 1)).size() >= 3 - (playerModel.getStationsLeft()-1);
        }

        return canClaim;
    }
}
