package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
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
    public List<DestinationCard> askDestinationCards(List<DestinationCard> cards) {
        return actionsController.askDestinationCards(cards);
    }

    @Override
    public void actionRefused(Action action) {
        actionsController.actionRefused(action);

    }

    @Override
    public void actionCompleted(Action action) {
        actionsController.actionCompleted(action);
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
    public void receivedDestinationCards(List<DestinationCard> destinationCards) {
        modelController.receivedDestinationCards(destinationCards);
    }
}
