package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;

public class RoutesController extends Controller {
    private static final int MINIMUM_PLAYERS_TO_KEEP_DOUBLE_ROUTES = 3;
    private final IRoutesControllerGameModel gameModel;

    public RoutesController(IRoutesControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    private Route getRoute(ClaimRoute wantedRoute) {
        if(wantedRoute == null || wantedRoute.route() == null) return null;
        Route route = gameModel.getRoute(wantedRoute.route().getId());
        return route != null && !route.isClaimed() && route.getLength() == wantedRoute.wagonCards().size() ? route : null;
    }

    @Override
    public boolean init(Player player) {
        return gameModel.setAllRoutesNotClaimed();
    }

    @Override
    public boolean doAction(Player player) {
        ClaimRoute claimRoute = player.askClaimRoute();
        Route route = getRoute(claimRoute);
        if (route == null) {
            return false;
        }

        List<WagonCard> wagonCards = claimRoute.wagonCards();

        List<WagonCard> removedCards = player.removeWagonCards(wagonCards);

        if (removedCards.size() != route.getLength()) {
            player.replaceRemovedWagonCards(removedCards);
            return false;
        }

        Color routeColor = route.getColor();

        if(routeColor != Color.ANY) {
            for(WagonCard card : removedCards) {
                if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                    player.replaceRemovedWagonCards(removedCards);
                    return false;
                }
            }
        }

        gameModel.discardWagonCards(removedCards);

        route.setClaimerPlayer(player.getIdentification());

        if(gameModel.getNbOfPlayers() >= MINIMUM_PLAYERS_TO_KEEP_DOUBLE_ROUTES) {
            Route doubleRoute = gameModel.getDoubleRouteOf(route.getId());
            if(doubleRoute != null) {
                gameModel.deleteRoute(doubleRoute.getId());
            }
        }

        player.notifyClaimedRoute(route);

        return true;
    }
}
