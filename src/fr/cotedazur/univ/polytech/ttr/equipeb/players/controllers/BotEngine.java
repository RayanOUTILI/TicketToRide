package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

public abstract class BotEngine implements IPlayerActionsControllable {
    protected final IPlayerGameModel gameModel;
    protected final IPlayerModel playerModel;
    protected final IPlayerEngineViewable view;

    protected final RandomGenerator random;

    protected BotEngine(IPlayerModel playerModel, IPlayerGameModel gameModel, IPlayerEngineViewable view, RandomGenerator random) {
        this.gameModel = gameModel;
        this.playerModel = playerModel;
        this.view = view;
        this.random = random;
    }

    @Override
    public void actionRefused(Action action, ReasonActionRefused reason) {
        if(view != null) {
            view.displayActionRefused(action, reason);
        }
    }

    @Override
    public void actionCompleted(Action action) {
        if(view != null) {
            view.displayActionCompleted(action);
        }
    }
}