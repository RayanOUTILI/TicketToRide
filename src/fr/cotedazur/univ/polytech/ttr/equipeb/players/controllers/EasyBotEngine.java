package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;


public class EasyBotEngine extends BotEngine {

    public EasyBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view) {
        super(playerModel, gameModel, view, new RandomGenerator());
    }

    protected EasyBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view, RandomGenerator random) {
        super(playerModel, gameModel, view, random);
    }


    /**
     * Determines the next action for the bot to take.
     *
     * @return the next action for the bot to take
     */
    @Override
    public Action askAction() {
        int action = random.nextInt(2);

        if (action == 0 && !gameModel.isDestinationCardDeckEmpty() && playerModel.getDestinationCards().size() < 3) {
            return Action.PICK_DESTINATION_CARDS;
        } else if (canTakeARoute()) {
            return Action.CLAIM_ROUTE;
        } else if (playerModel.getStationsLeft() > 0 && playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft() - 1)).size() == 3 - (playerModel.getStationsLeft() - 1)) {
            return Action.PLACE_STATION;
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
