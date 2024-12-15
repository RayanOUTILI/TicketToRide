package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

/**
 * A record that encapsulates both the controllable actions and the controllable
 * player model.
 */
public record PlayerController(IPlayerActionsControllable actionsController, IPlayerModelControllable modelController) {
}
