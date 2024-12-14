package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.Victory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.IPlayerActionsControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.PlayerEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
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

        IPlayerActionsControllable currentPlayer = new PlayerEngine(playerModel, gameModel);
        IPlayerModelControllable playerModelControllable = playerModel;

        Victory victory;
        while((victory = victoryController.endGame()) == null) {
            Action action = currentPlayer.askAction();
            switch (action) {
                case PICK_WAGON_CARD:
                    pickWagonCard(playerModelControllable);
                    break;
                case CLAIM_ROUTE:
                    ClaimRoute claimRoute = currentPlayer.askClaimRoute();
                    boolean done = claimRoute(playerModelControllable, claimRoute);
                    if(!done) currentPlayer.claimRouteRefused(claimRoute);
                    break;

            }
        }
        System.out.println("end Game reason : " + victory.reason());
    }

    protected void pickWagonCard(IPlayerModelControllable player) {
        player.receivedWagonCard(gameModel.getWagonCardDeck().drawCard());
    }

    protected boolean claimRoute(IPlayerModelControllable player, ClaimRoute claimRoute) {
        Route route = routesController.canClaimRoute(claimRoute);
        if(route != null) {
            int removedCards = wagonCardsController.removeWagonCardsToPlayer(player, claimRoute.wagonCards());
            if(removedCards == claimRoute.wagonCards().size()) {
                routesController.claimRoute(player, route);
                return true;
            }
        }
        return false;
    }
}
