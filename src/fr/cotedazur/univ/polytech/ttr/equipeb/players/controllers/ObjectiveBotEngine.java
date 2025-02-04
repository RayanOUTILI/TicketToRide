package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ObjectiveBotEngine extends BotModelControllable {

    public ObjectiveBotEngine(IPlayerGameModel gameModel, IPlayerModel playerModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
    }

    @Override
    public Action askAction() {
        if (false) {
            return Action.PICK_WAGON_CARD;
        }
        if (false) {
            return Action.CLAIM_ROUTE;
        }
        if (false) {
            return Action.PICK_DESTINATION_CARDS;
        }
        if (false) {
            return Action.PICK_DESTINATION_CARDS;
        }
        if (false) {
            return Action.PICK_WAGON_CARD;
        }
        return Action.STOP;
    }

    @Override
    protected RouteReadOnly chooseRoute() {
        return null;
    }

    @Override
    protected CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities) {
        return null;
    }

    @Override
    protected List<ShortDestinationCard> chooseDestinationCards(List<ShortDestinationCard> cards) {
       if (playerModel.getDestinationCards().isEmpty()){
              return chooseInitialDestinationCards(cards);
       }
       return (cards);
    }

    private List<ShortDestinationCard> chooseInitialDestinationCards(List<ShortDestinationCard> cards) {
        List<DestinationCard> bestPair = findDestinationsWithMostCommonCities(cards);
        if (bestPair.isEmpty()) {
            cards.sort((card1, card2) -> Integer.compare(card2.get(), card1.getScore()));
            return cards.subList(0, Math.min(2, cards.size()));
        }
    }

    @Override
    protected WagonCard getWantedWagonCard(List<WagonCard> shownCards) {
        return null;
    }

    @Override
    protected RouteReadOnly chooseRouteFromCity(List<RouteReadOnly> availableRoutes) {
        return null;
    }

    /**
     * Used to determine which destination cards to keep.
     * @param destinationCards the list of destination cards available to the bot.
     * @return the list of the destination cards with the most common cities between them.
     * If there are no destination cards with common cities, an empty list is returned.
     */
    private List<DestinationCard> findDestinationsWithMostCommonCities(List<DestinationCard> destinationCards){
        int maxCommon = 0;
        List<DestinationCard> bestPair = new ArrayList<>();
        bestPair.add(null);
        bestPair.add(null);

        for (int i = 0; i < destinationCards.size(); i++) {
            for (int j = i + 1; j < destinationCards.size(); j++) {
                int commonCount = countCommonCities(destinationCards.get(i), destinationCards.get(j));
                if (commonCount > maxCommon) {
                    maxCommon = commonCount;
                    bestPair.set(0,destinationCards.get(i));
                    bestPair.set(1,destinationCards.get(j));
                }
            }
        }
        return bestPair;
    }

    private int countCommonCities(DestinationCard dest1, DestinationCard dest2) {
        List<City> attributes1 = new ArrayList<>(dest1.getCities());
        List<City> attributes2 = new ArrayList<>(dest2.getCities());
        attributes1.retainAll(attributes2);
        return attributes1.size();
    }
}