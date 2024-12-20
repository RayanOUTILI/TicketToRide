package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;

import java.util.List;

/**
 * Interface to control the player actions
 */
public interface IPlayerActionsControllable {
    Action askAction();

    ClaimRoute askClaimRoute();

    List<ShortDestinationCard> askDestinationCards(List<ShortDestinationCard> cards);

    void actionRefused(Action action);

    void actionCompleted(Action action);

}
