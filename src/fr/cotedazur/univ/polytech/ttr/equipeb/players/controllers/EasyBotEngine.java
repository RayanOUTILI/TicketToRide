package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.ArrayList;
import java.util.List;

public class EasyBotEngine extends BotModelControllable {

    public EasyBotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
    }

    public EasyBotEngine(PlayerModel playerModel, GameModel gameModel) {
        super(gameModel, playerModel, null);
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
        // Else The bot will try to place a station if it can
        } else if (playerModel.getStationsLeft() > 0 && playerModel.getWagonCardsIncludingAnyColor(3- (playerModel.getStationsLeft()-1)).size() == 3- (playerModel.getStationsLeft()-1)) {
            return Action.PLACE_STATION;
        // Else, the bot will pick a wagon card
        } else if(!gameModel.isWagonCardDeckEmpty()) {
            return Action.PICK_WAGON_CARD;
        }
        else if(!gameModel.isDestinationCardDeckEmpty()) {
            return Action.PICK_DESTINATION_CARDS;
        }
        else {
            return Action.STOP;
        }
    }

    protected RouteReadOnly chooseRoute() {
        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAvailableRoutes().stream().filter(this::canTakeRoute).toList();
        if (availableRoutes.isEmpty()) return null;
        int randomIndex = random.nextInt(availableRoutes.size());
        return availableRoutes.get(randomIndex);
    }

    @Override
    protected CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities) {
        int cityIndex = random.nextInt(availableCities.size());
        return availableCities.get(cityIndex);
    }

    @Override
    protected List<ShortDestinationCard> chooseDestinationCards(List<ShortDestinationCard> cards) {
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
    protected WagonCard getWantedWagonCard(List<WagonCard> shownCards) {
        return shownCards.get(random.nextInt(shownCards.size()));
    }

    @Override
    protected RouteReadOnly chooseRouteFromCity(List<RouteReadOnly> availableRoutes) {
        int randomIndex = random.nextInt(availableRoutes.size());
        return availableRoutes.get(randomIndex);
    }

    private boolean canTakeARoute() {
        return chooseRoute() != null;
    }

    private boolean canTakeRoute(RouteReadOnly route) {
        if(route.isClaimed()) return false;

        if(playerModel.getNumberOfWagons() < route.getLength()) return false;

        if(route.getType() == RouteType.FERRY) {
            return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), route.getNbLocomotives()).size() == route.getLength();
        }
        else if (route.getType() == RouteType.TRAIN) {
            return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0).size() == route.getLength();
        }
        // Tunnel
        return playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), 0).size() >= route.getLength();
    }


}
