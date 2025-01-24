package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.IPlayerActionsControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

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
    public void receivedDestinationCards(List<ShortDestinationCard> destinationCards) {
        modelController.receivedDestinationCards(destinationCards);
    }

    @Override
    public List<DestinationCard> getDestinationCardsHand() {
        return modelController.getDestinationCardsHand();
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
