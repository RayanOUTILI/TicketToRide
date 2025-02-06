package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.List;
import java.util.Optional;

public abstract class BotEngineControllable extends BotEngine {


    protected BotEngineControllable(IPlayerGameModel gameModel, IPlayerModel playerModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
    }

    @Override
    public ClaimObject<RouteReadOnly> askClaimRoute() {
        RouteReadOnly route = chooseRoute();

        if (route == null) return null;

        return new ClaimObject<>(route, playerModel.getWagonCardsIncludingAnyColor(route.getColor(), route.getLength(), route.getType() == RouteType.FERRY ? route.getNbLocomotives() : 0));
    }

    /**
     * Chooses a city where the bot will place a station from the available cities.
     *
     * @return the claim station action for the chosen city.
     */
    @Override
    public ClaimObject<CityReadOnly> askClaimStation() {
        List<CityReadOnly> availableCities = gameModel.getNonControllableAvailableCities();
        if (availableCities.isEmpty()) {
            return null;
        }
        CityReadOnly bestCity = chooseCityToPlaceStation(availableCities);
        return new ClaimObject<>(bestCity, playerModel.getWagonCardsIncludingAnyColor(3 - (playerModel.getStationsLeft() - 1)));
    }

    /**
     * Determines the destination cards the bot will keep based on their priority.
     *
     * @param cards the list of destination cards available to the bot.
     * @return a list of selected destination cards.
     */
    @Override
    public List<DestinationCard> askDestinationCards(List<DestinationCard> cards) {
        return chooseDestinationCards(cards);
    }

    @Override
    public List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor) {
        return playerModel.getWagonCardsOfColor(acceptedColor, numberOfCards);
    }

    @Override
    public Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions) {
        if (possibleActions.isEmpty()) return Optional.empty();
        return chooseActionDrawWagonCard(possibleActions);
    }

    protected abstract Optional<ActionDrawWagonCard> chooseActionDrawWagonCard(List<ActionDrawWagonCard> possibleActions);

    @Override
    public WagonCard askWagonCardFromShownCards() {
        List<WagonCard> shownCards = gameModel.getListOfShownWagonCards();
        return getWantedWagonCard(shownCards);
    }

    @Override
    public RouteReadOnly askChooseRouteStation(CityReadOnly city) {
        List<RouteReadOnly> availableRoutes = gameModel.getNonControllableAdjacentRoutes(city);
        if (availableRoutes.isEmpty()) return null;
        return chooseRouteFromCity(availableRoutes);
    }

    @Override
    public List<DestinationCard> askInitialDestinationCards(List<DestinationCard> cards) {
        return chooseInitialDestinationCards(cards);
    }

    /**
     * Determines the destination cards the bot will keep at the beginning of the game.
     *
     * @param cards the list of destination cards available to the bot.
     * @return a list of selected destination cards.
     */
    protected abstract List<DestinationCard> chooseInitialDestinationCards(List<DestinationCard> cards);

    /**
     * Determines the route the bot will take next.
     *
     * @return the next action for the bot to take
     */
    protected abstract RouteReadOnly chooseRoute();

    /**
     * Chooses the city for placing a station from a list of available cities.
     *
     * @param availableCities the list of cities where the bot can place a station.
     * @return the city to place the station in.
     */
    protected abstract CityReadOnly chooseCityToPlaceStation(List<CityReadOnly> availableCities);

    /**
     * Chooses the destination cards the bot will keep.
     *
     * @param cards the list of destination cards available to the bot.
     * @return a list of selected destination cards.
     */
    protected abstract List<DestinationCard> chooseDestinationCards(List<DestinationCard> cards);

    /**
     * Chooses the wagon card the bot wants from the shown cards.
     *
     * @param shownCards the list of shown wagon cards.
     * @return the wagon card the bot wants.
     */
    protected abstract WagonCard getWantedWagonCard(List<WagonCard> shownCards);

    /**
     * Chooses the route the bot will take from a list of available routes,
     * coming from a specific city.
     *
     * @param availableRoutes the list of available routes.
     * @return the route the bot will take.
     */
    protected abstract RouteReadOnly chooseRouteFromCity(List<RouteReadOnly> availableRoutes);

    @Override
    public void actionRefused(Action action, ReasonActionRefused reason) {
        view.ifPresent(iPlayerEngineViewable -> iPlayerEngineViewable.displayActionRefused(action, reason));
    }

    @Override
    public void actionSkipped(Action action, ReasonActionRefused reason) {
        view.ifPresent(iPlayerEngineViewable -> iPlayerEngineViewable.displayActionSkipped(action, reason));
    }

    @Override
    public void actionCompleted(Action action) {
        view.ifPresent(iPlayerEngineViewable -> iPlayerEngineViewable.displayActionCompleted(action));
    }

    @Override
    public void actionStop() {
        view.ifPresent(IPlayerEngineViewable::displayActionStop);
    }
}
