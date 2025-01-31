package fr.cotedazur.univ.polytech.ttr.equipeb.engine;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.EndGameAction;
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
import fr.cotedazur.univ.polytech.ttr.equipeb.views.ScoreConsoleView;

import java.util.*;

public class GameEngine {
    private final ScoreController scoreController;
    private final GameModel gameModel;
    private final IGameViewable gameView;
    private final Map<Action, Controller> gameControllers;
    private final Map<EndGameAction, Controller> endGameControllers;
    private final List<Player> players;
    private Iterator<Player> playerIterator;
    private Optional<PlayerIdentification> lastTurnPlayer;

    private Player currentPlayer;
    public GameEngine(GameModel gameModel, List<Player> players) {
        this.gameModel = gameModel;
        this.players = players;

        this.gameView = new GameConsoleView();
        this.gameControllers = Map.of(
            Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
            Action.CLAIM_ROUTE, new RoutesController(gameModel),
            Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel),
            Action.PLACE_STATION, new StationController(gameModel)
        );
        this.endGameControllers = Map.of(
            EndGameAction.CHOOSE_ROUTE_STATION, new ChooseRouteStationController(gameModel)
        );
        this.scoreController = new ScoreController(gameModel, new ScoreConsoleView());
        this.playerIterator = players.iterator();
        this.currentPlayer = playerIterator.next();

        this.lastTurnPlayer = Optional.empty();
    }

    public boolean initGame() {
        boolean success;

        Set<Map.Entry<Action, Controller>> entries = gameControllers.entrySet();
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

            Iterator<Map.Entry<Action, Controller>> entries = gameControllers.entrySet().iterator();
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
        int nbPlayersWantStop = 0;

        while(!isWasTheLastTurn() && !forcedEndGame) {
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

            boolean newTurn = nextPlayer();

            if (newTurn){
                scoreController.calculatePlacedRoutesScore(currentPlayer);

                if(gameModel.isWagonCardDeckEmpty()) gameModel.fillWagonCardDeck();

                nbTurn++;
            }
        }

        askForEndGameActions();

        scoreController.setFinalScores();

        lastTurnPlayer.ifPresent(playerIdentification -> gameView.displayEndGameReason(playerIdentification, currentPlayer.getNumberOfWagons()));

        PlayerModel winner = gameModel.getWinner();

        if(winner != null) gameView.displayWinner(winner.getIdentification(), winner.getScore());

        return nbTurn;
    }

    private void askForEndGameActions() {
        for(Map.Entry<EndGameAction, Controller> entry : endGameControllers.entrySet()) {

            for (Player player : players) {
                Optional<ReasonActionRefused> endGameAction;
                do {
                    endGameAction = entry.getValue().doAction(player);
                } while (endGameAction.isPresent());
            }
        }
    }

    private boolean isWasTheLastTurn() {
        return lastTurnPlayer.isPresent() && currentPlayer.getIdentification() == lastTurnPlayer.get();
    }

    private TypeActionHandled handlePlayerAction(Player player) {
        Action action = player.askAction();
        if(action == Action.STOP) return TypeActionHandled.STOP;

        else if(action == null || !gameControllers.containsKey(action)) {
            player.actionRefused(action, ReasonActionRefused.ACTION_INVALID);
            return TypeActionHandled.REFUSED;
        }

        Controller controller = gameControllers.get(action);
        Optional<ReasonActionRefused> actionRefused = controller.doAction(player);

        if (actionRefused.isPresent()) {
            player.actionRefused(action, actionRefused.get());
        }
        else  player.actionCompleted(action);
        return actionRefused.isEmpty() ? TypeActionHandled.SUCCESS : TypeActionHandled.REFUSED;
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
