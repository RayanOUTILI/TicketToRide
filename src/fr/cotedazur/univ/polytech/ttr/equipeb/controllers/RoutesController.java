package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoutesController extends Controller {
    private static final int THRESHOLD_PLAYERS_TO_KEEP_DOUBLE_ROUTES = 4;
    private static final int STARTING_WAGON_CARDS = 45;
    private final IRoutesControllerGameModel gameModel;

    public RoutesController(IRoutesControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean initGame() {
        return gameModel.setAllRoutesIDs();
    }

    @Override
    public boolean initPlayer(Player player) {
        return player.setNumberOfWagons(STARTING_WAGON_CARDS);
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        ClaimObject<RouteReadOnly> claimRoute = player.askClaimRoute();

        if(claimRoute == null || claimRoute.getClaimable() == null) {
            return Optional.of(ReasonActionRefused.ROUTE_INVALID);
        }

        Route route = gameModel.getRoute(claimRoute.getClaimable().getId());

        if(route == null) return Optional.of(ReasonActionRefused.ROUTE_WANTED_ROUTE_NOT_FOUND);

        if(route.isClaimed()) return Optional.of(ReasonActionRefused.ROUTE_WANTED_ROUTE_ALREADY_CLAIMED);

        if(player.getNumberOfWagons() < route.getLength()) return Optional.of(ReasonActionRefused.ROUTE_NOT_ENOUGH_WAGONS);

        if(route.getLength() != claimRoute.wagonCards().size()) return Optional.of(ReasonActionRefused.ROUTE_WRONG_GIVEN_WAGON_CARDS_SIZE);

        List<WagonCard> wagonCards = claimRoute.wagonCards();

        List<WagonCard> removedCards = player.removeWagonCards(wagonCards);

        if (removedCards.size() != route.getLength()) {
            player.replaceRemovedWagonCards(removedCards);
            return Optional.of(ReasonActionRefused.ROUTE_NOT_ENOUGH_WAGON_CARDS);
        }

        Optional<ReasonActionRefused> refused = switch (route.getType()) {
            case FERRY -> claimFerry(route, removedCards);
            case TRAIN -> claimTrain(route, removedCards);
            case TUNNEL -> claimTunnel(player, route, removedCards);
        };

        if(refused.isPresent()) {
            player.replaceRemovedWagonCards(removedCards);
            return refused;
        }

        gameModel.discardWagonCards(removedCards);

        player.removeWagons(route.getLength());

        route.setClaimerPlayer(player.getIdentification());

        if(gameModel.getNbOfPlayers() < THRESHOLD_PLAYERS_TO_KEEP_DOUBLE_ROUTES) {
            Route doubleRoute = gameModel.getDoubleRouteOf(route.getId());
            if(doubleRoute != null) {
                gameModel.deleteRoute(doubleRoute.getId());
            }
        }

        player.notifyClaimedRoute(route);

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        return player.clearNumberOfWagons();
    }

    @Override
    public boolean resetGame() {
        return gameModel.retrieveDeletedRoutes() && gameModel.setAllRoutesNotClaimed();
    }

    private Optional<ReasonActionRefused> claimFerry(Route route, List<WagonCard> removedCards) {
        ArrayList<WagonCard> locomotives = new ArrayList<>();

        Color routeColor = route.getColor();

        for(int i = 0; i < removedCards.size() && locomotives.size() < route.getNbLocomotives(); i++) {
            if(removedCards.get(i).getColor() == Color.ANY) {
                locomotives.add(removedCards.get(i));
            }
        }

        if(locomotives.size() < route.getNbLocomotives()) {
            return Optional.of(ReasonActionRefused.ROUTE_FERRY_NOT_ENOUGH_LOCOMOTIVES);
        }

        removedCards.removeAll(locomotives);

        for(WagonCard card : removedCards) {
            if(routeColor == Color.ANY) routeColor = card.getColor();
            if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                removedCards.addAll(locomotives);
                return Optional.of(ReasonActionRefused.ROUTE_FERRY_WRONG_WAGON_CARDS_COLOR);
            }
        }



        return Optional.empty();
    }


    private Optional<ReasonActionRefused> claimTunnel(Player player, Route route, List<WagonCard> removedCards) {
        Color routeColor = route.getColor();

        for(WagonCard card : removedCards) {
            if(routeColor == Color.ANY) routeColor = card.getColor();
            if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                return Optional.of(ReasonActionRefused.ROUTE_TUNNEL_WRONG_WAGON_CARDS_COLOR);
            }
        }
        List<WagonCard> drawnCards = gameModel.drawCardsFromWagonCardDeck(3);
        if(!drawnCards.isEmpty()) {
            Color finalRouteColor = routeColor;

            int nbCardsToAdd = drawnCards.stream().filter(card -> card.getColor() == finalRouteColor).toArray().length;

            List<WagonCard> wagonCardsForTunnel = player.askWagonCardsForTunnel(nbCardsToAdd, finalRouteColor);
            List<WagonCard> removedCardsForTunnel = player.removeWagonCards(wagonCardsForTunnel);

            removedCards.addAll(removedCardsForTunnel);

            if(removedCardsForTunnel.size() < nbCardsToAdd) {
                gameModel.discardWagonCards(drawnCards);
                return Optional.of(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_WAGON_CARDS);
            }

            long nbCardsRemovedWithWantedColor = removedCardsForTunnel.stream().filter(wagonCard -> wagonCard.getColor() == finalRouteColor).count();
            
            if(nbCardsRemovedWithWantedColor != removedCardsForTunnel.size()) {
                gameModel.discardWagonCards(drawnCards);
                return Optional.of(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_REMOVED_WAGON_CARDS);
            }
        }

        gameModel.discardWagonCards(drawnCards);

        return Optional.empty();
    }

    private Optional<ReasonActionRefused> claimTrain(Route route, List<WagonCard> removedCards) {
        Color routeColor = route.getColor();

        for(WagonCard card : removedCards) {
            if(routeColor == Color.ANY) routeColor = card.getColor();
            if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                return Optional.of(ReasonActionRefused.ROUTE_TRAIN_WRONG_WAGON_CARDS_COLOR);
            }
        }

        return Optional.empty();
    }
}
