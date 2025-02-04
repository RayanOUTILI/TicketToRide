package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.IPlayerActionsControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;
import java.util.Optional;

/**
 * A record that encapsulates both the controllable actions and the controllable
 * player model.
 */
public class Player implements IPlayerActionsControllable, IPlayerModelControllable {
    private final IPlayerActionsControllable actionsController;
    private final IPlayerModelControllable modelController;

    public Player(IPlayerActionsControllable actionsController, IPlayerModelControllable modelController) {
        this.actionsController = actionsController;
        this.modelController = modelController;
    }

    public IPlayerModelControllable getModelController() {
        return modelController;
    }

    @Override
    public Action askAction() {
        return actionsController.askAction();
    }

    @Override
    public ClaimRoute askClaimRoute() {
        return actionsController.askClaimRoute();
    }

    @Override
    public ClaimStation askClaimStation() {
        return actionsController.askClaimStation();
    }

    @Override
    public List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards) {
        return actionsController.askDestinationCards(cards);
    }

    @Override
    public void actionRefused(Action action, ReasonActionRefused reason) {
        actionsController.actionRefused(action, reason);
    }

    @Override
    public void actionCompleted(Action action) {
        actionsController.actionCompleted(action);
    }

    @Override
    public List<WagonCard> askWagonCardsForTunnel(int numberOfCards, Color acceptedColor) {
        return actionsController.askWagonCardsForTunnel(numberOfCards, acceptedColor);
    }

    @Override
    public Optional<ActionDrawWagonCard> askDrawWagonCard(List<ActionDrawWagonCard> possibleActions) {
        return actionsController.askDrawWagonCard(possibleActions);
    }

    @Override
    public WagonCard askWagonCardFromShownCards() {
        return actionsController.askWagonCardFromShownCards();
    }

    @Override
    public RouteReadOnly askChooseRouteStation(CityReadOnly city) {
        return actionsController.askChooseRouteStation(city);
    }

    @Override
    public PlayerIdentification getIdentification() {
        return modelController.getIdentification();
    }

    @Override
    public void receivedWagonCard(WagonCard wagonCard) {
        modelController.receivedWagonCard(wagonCard);
    }

    @Override
    public void receivedWagonCards(List<WagonCard> wagonCards) {
        modelController.receivedWagonCards(wagonCards);
    }

    @Override
    public List<WagonCard> removeWagonCards(List<WagonCard> wagonCards) {
        return modelController.removeWagonCards(wagonCards);
    }

    @Override
    public List<WagonCard> getWagonCardsHand() {
        return modelController.getWagonCardsHand();
    }

    @Override
    public void replaceRemovedWagonCards(List<WagonCard> wagonCards) {
        modelController.replaceRemovedWagonCards(wagonCards);
    }

    @Override
    public void notifyClaimedRoute(RouteReadOnly route) {
        modelController.notifyClaimedRoute(route);
    }

    @Override
    public void notifyClaimedStation(CityReadOnly city, List<WagonCard> wagonCards) {
        modelController.notifyClaimedStation(city, wagonCards);
    }

    @Override
    public boolean setNumberOfWagons(int startingWagonCards) {
        return modelController.setNumberOfWagons(startingWagonCards);
    }

    @Override
    public int getNumberOfWagons() {
        return modelController.getNumberOfWagons();
    }

    @Override
    public void removeWagons(int numberOfWagons) {
        modelController.removeWagons(numberOfWagons);
    }

    @Override
    public void addChosenRouteStation(RouteReadOnly route) {
        modelController.addChosenRouteStation(route);
    }

    @Override
    public List<RouteReadOnly> getSelectedStationRoutes() {
        return modelController.getSelectedStationRoutes();
    }

    @Override
    public boolean clearDestinationCards() {
        return modelController.clearDestinationCards();
    }

    @Override
    public boolean clearChosenRouteStations() {
        return modelController.clearChosenRouteStations();
    }

    @Override
    public boolean clearScore() {
        return modelController.clearScore();
    }

    @Override
    public boolean clearStationsLeft() {
        return modelController.clearStationsLeft();
    }

    @Override
    public boolean clearNumberOfWagons() {
        return modelController.clearNumberOfWagons();
    }

    @Override
    public void receivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        modelController.receivedDestinationCards(destinationCards);
    }

    @Override
    public void receiveLongDestCards(List<LongDestinationCard> destinationCards) {
        modelController.receiveLongDestCards(destinationCards);
    }

    @Override
    public List<DestinationCard> getDestinationCardsHand() {
        return modelController.getDestinationCardsHand();
    }

    @Override
    public List<ShortDestinationCard> getShortDestinationCardsHand() {
        return modelController.getShortDestinationCardsHand();
    }

    @Override
    public List<LongDestinationCard> getLongDestinationCardsHand() {
        return modelController.getLongDestinationCardsHand();
    }

    @Override
    public void defineStartingStationsNumber(int size) {
        modelController.defineStartingStationsNumber(size);
    }

    @Override
    public int getStationsLeft() {
        return modelController.getStationsLeft();
    }

    @Override
    public void decrementStationsLeft() {
        modelController.decrementStationsLeft();
    }

    @Override
    public void setScore(int score) {
        modelController.setScore(score);
    }

    @Override
    public int getScore() {
        return modelController.getScore();
    }
}
