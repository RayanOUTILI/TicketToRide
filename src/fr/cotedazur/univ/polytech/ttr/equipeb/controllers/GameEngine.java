package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerController;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

public class GameEngine {
    private final RoutesController routesController;
    private final WagonCardsController wagonCardsController;
    private final VictoryController victoryController;
    private final GameModel gameModel;
    private final IGameViewable gameView;

    public GameEngine(GameModel gameModel) {
        this.gameModel = gameModel;
        this.routesController = new RoutesController(gameModel);
        this.wagonCardsController = new WagonCardsController(gameModel);
        this.victoryController = new VictoryController(gameModel);
        this.gameView = new GameConsoleView();
    }

    public void startGame(PlayerModel playerModel) {

        PlayerController playerController = new PlayerController(new PlayerEngine(playerModel, gameModel), playerModel);

        Victory victory;
        while((victory = victoryController.endGame()) == null) {
            handlePlayerAction(playerController);
        }
        gameView.displayEndGameReason(victory.reason());
    }

    private void handlePlayerAction(PlayerController playerController) {
        Action action = playerController.actionsController().askAction();

        switch (action) {
            case PICK_WAGON_CARD -> wagonCardsController.pickWagonCard(playerController);
            case CLAIM_ROUTE -> handleClaimRoute(playerController);
            default -> throw new IllegalStateException("Action non supportée: " + action);
        }
    }

    private void handleClaimRoute(PlayerController playerController) {
        ClaimRoute claimRoute = playerController.actionsController().askClaimRoute();

        boolean success = routesController.attemptClaimRoute(playerController, claimRoute, wagonCardsController);

        if (!success) {
            playerController.actionsController().claimRouteRefused(claimRoute);
        }
    }
}
