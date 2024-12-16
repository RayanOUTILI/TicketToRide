package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerController;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

public class GameEngine {
    private final RoutesController routesController;
    private final WagonCardsController wagonCardsController;
    private final VictoryController victoryController;
    private final GameModel gameModel;

    public GameEngine(GameModel gameModel) {
        this.gameModel = gameModel;
        this.routesController = new RoutesController(gameModel);
        this.wagonCardsController = new WagonCardsController(gameModel);
        this.victoryController = new VictoryController(gameModel);
    }

    public void startGame(PlayerModel playerModel) {

        PlayerController playerController = new PlayerController(new PlayerEngine(playerModel, gameModel), playerModel);

        Victory victory;
        while((victory = victoryController.endGame()) == null) {
            Action action = playerController.actionsController().askAction();
            switch (action) {
                case PICK_WAGON_CARD:
                    pickWagonCard(playerController);
                    break;
                case CLAIM_ROUTE:
                    ClaimRoute claimRoute = playerController.actionsController().askClaimRoute();
                    boolean done = claimRoute(playerController, claimRoute);
                    if(!done) playerController.actionsController().claimRouteRefused(claimRoute);
                    break;

            }
        }
        System.out.println("end Game reason : " + victory.reason());
    }

    protected void pickWagonCard(PlayerController player) {
        player.modelController().receivedWagonCard(gameModel.getWagonCardDeck().drawCard());
    }

    protected boolean claimRoute(PlayerController player, ClaimRoute claimRoute) {
        Route route = routesController.canClaimRoute(claimRoute);
        if(route != null) {
            int removedCards = wagonCardsController.removeWagonCardsToPlayer(player.modelController(), claimRoute.wagonCards());
            if(removedCards == claimRoute.wagonCards().size()) {
                routesController.claimRoute(player.modelController(), route);
                return true;
            }
        }
        return false;
    }
}
