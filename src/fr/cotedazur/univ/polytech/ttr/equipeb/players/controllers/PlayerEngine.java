package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

public class PlayerEngine implements IPlayerActionsControllable {
    private final IPlayerGameModel gameModel;
    private final IPlayerModel playerModel;

    public PlayerEngine(PlayerModel playerModel, IPlayerGameModel gameModel) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
    }


    @Override
    public Action askAction() {
        if(playerModel.getNumberOfWagonCards() < 3) return Action.PICK_WAGON_CARD;
        else return Action.CLAIM_ROUTE;
    }

    @Override
    public ClaimRoute askClaimRoute() {
        return new ClaimRoute(gameModel.getNonControllableRoutes().getFirst(), playerModel.getWagonCards(3));
    }

    @Override
    public void claimRouteRefused(ClaimRoute claimRoute) {
        System.out.println("Route refused");
    }
}
