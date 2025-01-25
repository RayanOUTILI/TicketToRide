package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.console;
import static java.lang.System.exit;

public class EasyBotEngine implements IPlayerActionsControllable {
    private final IPlayerGameModel gameModel;
    private final IPlayerModel playerModel;
    private final IPlayerEngineViewable view;

    private final RandomGenerator random;

    public EasyBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view) {
        this(playerModel, gameModel, view, new RandomGenerator());
    }

    protected EasyBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view, RandomGenerator random) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.view = view;
        this.random = random;
    }


    @Override
    public Action askAction() {
        int action = random.nextInt(2);

        // The bot will always try to pick destination cards if it can and has less than 3 cards
        if (action == 0 && !gameModel.isDestinationCardDeckEmpty() && playerModel.getDestinationCards().size() < 3) {
            return Action.PICK_DESTINATION_CARDS;
        // Else The bot will try to claim a route if it can
        } else if (canTakeARoute()) {
            return Action.CLAIM_ROUTE;
        // Else The bot will try to place a station if it can
        // TODO: change the condition to check if the player has enough cards to place a station
        } else if (playerModel.getStationsLeft() > 0 && playerModel.getWagonCardsIncludingAnyColor(3- (playerModel.getStationsLeft()-1)).size() == 3- (playerModel.getStationsLeft()-1)) {
            return Action.PLACE_STATION;
        // Else, the bot will pick a wagon card
        } else {
            return Action.PICK_WAGON_CARD;
        }
    }

    @Override
    public ClaimRoute askClaimRoute() {
        RouteReadOnly route = chooseRoute();

        if(route == null) return null;

        return new ClaimRoute(route, playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), route.getType() == RouteType.FERRY ? route.getNbLocomotives() : 0));
    }

    @Override
    public ClaimStation askClaimStation() {
        List<CityReadOnly> availableCities = gameModel.getNonControllableAvailableCities();

        int cityIndex = random.nextInt(availableCities.size());
        CityReadOnly city = availableCities.get(cityIndex);

        // TODO: find a proper way to get the right amount of cards
        return new ClaimStation(city, playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft()-1)));
    }

    @Override
    public List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards) {
        int maxCardsNumber = cards.size();

        List<ShortDestinationCard> cardsToKeep = new ArrayList<>(cards);

        if (!cards.isEmpty() && maxCardsNumber > 1) {
            int nbCardsToRemove = random.nextInt(maxCardsNumber - 1);

            // Remove random cards
            for (int i = nbCardsToRemove - 1; i >= 0; i--) {
                cardsToKeep.remove(random.nextInt(cardsToKeep.size()));
            }
        }

        return cardsToKeep;
    }

    @Override
    public void actionRefused(Action action, ReasonActionRefused reason) {
        if(view != null) {
            view.displayActionRefused(action, reason);
        }
    }

    @Override
    public void actionCompleted(Action action) {
        if(view != null) {
            view.displayActionCompleted(action);
        }
    }

    @Override
    public List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor) {
        return playerModel.getWagonCardsOfColor(acceptedColor, numberOfCards);
    }

    private RouteReadOnly chooseRoute() {
        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAvailableRoutes().stream().filter(this::canTakeRoute).toList();
        if(availableRoutes.isEmpty()) return null;
        int randomIndex = random.nextInt(availableRoutes.size());
        return availableRoutes.get(randomIndex);
    }

    private boolean canTakeARoute() {
        return chooseRoute() != null;
    }

    private boolean canTakeRoute(RouteReadOnly route) {
        if(route.isClaimed()) return false;

        if(route.getType() == RouteType.FERRY) {
            return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), route.getNbLocomotives()).size() == route.getLength();
        }
        else if (route.getType() == RouteType.TRAIN) {
            return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0).size() == route.getLength();
        }
        else if (route.getType() == RouteType.TUNNEL) {
            return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength()+1, 0).size() >= route.getLength();
        }

        return false;
    }


}
