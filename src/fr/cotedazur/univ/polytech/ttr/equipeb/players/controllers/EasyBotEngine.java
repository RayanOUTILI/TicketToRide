package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (action == 0 && !gameModel.isDestinationCardDeckEmpty()) {
            return Action.PICK_DESTINATION_CARDS;
        } else if (canTakeARoute()) {
            return Action.CLAIM_ROUTE;
        } else {
            return Action.PICK_WAGON_CARD;
        }
    }

    @Override
    public ClaimRoute askClaimRoute() {
        RouteReadOnly route = chooseRoute();

        if(route == null) return null;

        return new ClaimRoute(route, playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength()));
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
    public void actionRefused(Action action) {
        if(view != null) {
            view.displayActionRefused(action);
        }
    }

    @Override
    public void actionCompleted(Action action) {
        if(view != null) {
            view.displayActionCompleted(action);
        }
    }

    private RouteReadOnly chooseRoute() {
        return gameModel.getNonControllableAvailableRoutes().stream().filter(this::canTakeRoute).findAny().orElse(null);
    }

    private boolean canTakeARoute() {
        return chooseRoute() != null;
    }

    private boolean canTakeRoute(RouteReadOnly route) {
        if(route.isClaimed()) return false;

        return playerModel.getNumberOfWagonCardsIncludingAnyColor(route.getColor()) >= route.getLength();
    }


}
