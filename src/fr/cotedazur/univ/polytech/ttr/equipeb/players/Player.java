package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.IPlayerActionsControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

/**
 * A record that encapsulates both the controllable actions and the controllable
 * player model.
 */
public record Player(IPlayerActionsControllable actionsController, IPlayerModelControllable modelController) {
}
