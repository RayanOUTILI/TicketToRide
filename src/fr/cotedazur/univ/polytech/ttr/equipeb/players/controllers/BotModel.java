package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;

import java.util.Optional;

public abstract class BotModel implements IPlayerActionsControllable {
    protected final IPlayerGameModel gameModel;
    protected final IPlayerModel playerModel;
    protected final Optional<IPlayerEngineViewable> view;

    protected BotModel(IPlayerGameModel gameModel, IPlayerModel playerModel, IPlayerEngineViewable view) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.view = view != null ? Optional.of(view) : Optional.empty();
    }
}
