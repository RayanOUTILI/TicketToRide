package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;

public class RoutesController extends Controller {
    private final IRoutesControllerGameModel gameModel;

    public RoutesController(IRoutesControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    private Route getRoute(ClaimRoute wantedRoute) {
        if(wantedRoute == null) return null;
        Route route = gameModel.getRoutes().stream().filter(r -> r.getId() == wantedRoute.route().getId()).findFirst().orElse(null);
        return route != null && !route.isClaimed() && route.getLength() == wantedRoute.wagonCards().size() ? route : null;
    }

    @Override
    public boolean doAction(Player player) {
        ClaimRoute claimRoute = player.askClaimRoute();
        Route route = getRoute(claimRoute);
        if (route == null) return false;

        List<WagonCard> wagonCards = claimRoute.wagonCards();

        int removedCards = player.removeWagonCards(wagonCards);

        if (removedCards != wagonCards.size()) return false;


        route.setClaimerPlayer(player.getIdentification());
        player.notifyClaimedRoute(route);

        return true;
    }
}
