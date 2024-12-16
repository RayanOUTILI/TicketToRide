package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
/**
 * Interface to control the player actions
 */
public interface IPlayerActionsControllable {
    Action askAction();

    ClaimRoute askClaimRoute();

    void claimRouteRefused(ClaimRoute claimRoute);
}
