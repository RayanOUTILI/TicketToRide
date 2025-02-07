package fr.cotedazur.univ.polytech.ttr.equipeb.factories.game_actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.Controller;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;

import java.util.List;
import java.util.Map;

public abstract class GameActionsFactory {
    public abstract Map<Action, Controller> getGameActions(GameModel gameModel);
    public abstract List<Controller> getEndTurnActions(GameModel gameModel);
    public abstract List<Controller> getEndGameActions(GameModel gameModel, boolean display);
}
