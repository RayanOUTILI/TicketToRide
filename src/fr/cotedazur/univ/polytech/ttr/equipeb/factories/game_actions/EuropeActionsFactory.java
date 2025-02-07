package fr.cotedazur.univ.polytech.ttr.equipeb.factories.game_actions;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.ScoreConsoleView;

import java.util.List;
import java.util.Map;

public class EuropeActionsFactory extends GameActionsFactory {
    @Override
    public Map<Action, Controller> getGameActions(GameModel gameModel) {
        return Map.of(
                Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
                Action.CLAIM_ROUTE, new RoutesController(gameModel),
                Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel),
                Action.PLACE_STATION, new StationController(gameModel)
        );
    }

    @Override
    public List<Controller> getEndTurnActions(GameModel gameModel) {
        return List.of(
                new CurrentPlayerScoreController(gameModel)
        );
    }

    @Override
    public List<Controller> getEndGameActions(GameModel gameModel, boolean display) {
        return List.of(
                new ChooseRouteStationController(gameModel),
                new EndGameScoreController(gameModel, display ? new ScoreConsoleView() : null)
        );
    }
}
