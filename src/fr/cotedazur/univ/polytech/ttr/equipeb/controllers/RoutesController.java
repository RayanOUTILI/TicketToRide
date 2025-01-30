package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
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
        return gameModel.setAllRoutesNotClaimed();
    }

    @Override
    public boolean initPlayer(Player player) {
        return player.setNumberOfWagons(STARTING_WAGON_CARDS);
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        ClaimRoute claimRoute = player.askClaimRoute();

        if(claimRoute == null || claimRoute.route() == null) {
            return Optional.of(ReasonActionRefused.ROUTE_INVALID);
        }

        Route route = gameModel.getRoute(claimRoute.route().getId());

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

        Color routeColor = route.getColor();

        switch (route.getType()) {
            case RouteType.FERRY: {
                ArrayList<WagonCard> locomotives = new ArrayList<>();

                for(int i = 0; i < removedCards.size() && locomotives.size() < route.getNbLocomotives(); i++) {
                    if(removedCards.get(i).getColor() == Color.ANY) {
                        locomotives.add(removedCards.get(i));
                    }
                }

                if(locomotives.size() < route.getNbLocomotives()) {
                    player.replaceRemovedWagonCards(removedCards);
                    return Optional.of(ReasonActionRefused.ROUTE_FERRY_NOT_ENOUGH_LOCOMOTIVES);
                }

                removedCards.removeAll(locomotives);

                for(WagonCard card : removedCards) {
                    if(routeColor == Color.ANY) routeColor = card.getColor();
                    if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                        removedCards.addAll(locomotives);
                        player.replaceRemovedWagonCards(removedCards);
                        return Optional.of(ReasonActionRefused.ROUTE_FERRY_WRONG_WAGON_CARDS_COLOR);
                    }
                }

                break;
            }
            case RouteType.TRAIN: {
                for(WagonCard card : removedCards) {
                    if(routeColor == Color.ANY) routeColor = card.getColor();
                    if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                        player.replaceRemovedWagonCards(removedCards);
                        return Optional.of(ReasonActionRefused.ROUTE_TRAIN_WRONG_WAGON_CARDS_COLOR);
                    }
                }
                break;
            }
            case RouteType.TUNNEL: {
                for(WagonCard card : removedCards) {
                    if(routeColor == Color.ANY) routeColor = card.getColor();
                    if(card.getColor() != routeColor && card.getColor() != Color.ANY) {
                        player.replaceRemovedWagonCards(removedCards);
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
                        player.replaceRemovedWagonCards(removedCards);
                        gameModel.discardWagonCards(drawnCards);
                        return Optional.of(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_WAGON_CARDS);
                    }

                    int nbOfCardsToRemove = nbCardsToAdd;

                    for(WagonCard wagonCard : removedCardsForTunnel) {
                        if(wagonCard.getColor() == finalRouteColor) nbOfCardsToRemove--;
                    }

                    if(nbOfCardsToRemove != 0) {
                        player.replaceRemovedWagonCards(removedCards);
                        gameModel.discardWagonCards(drawnCards);
                        return Optional.of(ReasonActionRefused.ROUTE_TUNNEL_NOT_ENOUGH_REMOVED_WAGON_CARDS);
                    }
                }
                break;
            }
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
}
