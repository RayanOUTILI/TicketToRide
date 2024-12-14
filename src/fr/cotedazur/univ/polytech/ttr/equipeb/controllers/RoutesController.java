package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

public class RoutesController {
    private final GameModel gameModel;

    public RoutesController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public Route canClaimRoute(ClaimRoute wantedRoute) {
        Route route = (Route) wantedRoute.route();
        return route != null && !route.isClaimed() && route.getLength() == wantedRoute.wagonCards().size() ? route : null;
    }

    public boolean claimRoute(IPlayerModelControllable player, Route route) {
        route.setClaimerPlayer(player.getIdentification());
        player.notifyClaimedRoute(route);
        return true;
    }


}
