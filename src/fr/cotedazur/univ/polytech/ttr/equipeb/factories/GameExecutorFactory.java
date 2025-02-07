package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewOptions;
import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation.DataModelisationFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.game_actions.GameActionsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.players.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameExecutor;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.List;
import java.util.Optional;

public class GameExecutorFactory {

    private GameExecutorFactory() {
        throw new IllegalStateException("Non instanceable factory class");
    }

    public static GameExecutor createGame(
            DataModelisationFactory dataModelisationFactory,
            GameActionsFactory gameActionsFactory,
            PlayerFactory playerFactory,
            ViewFactory viewFactory,
            List<PlayerType> playersTypes,
            List<ViewOptions> viewOptions
    ) {
        List<IPlayerEngineViewable> playerEngineViews = viewFactory.createPlayerEngineViewsFor(viewOptions, playersTypes);
        List<IPlayerViewable> playerViews = viewFactory.createPlayerViewsFor(viewOptions, playersTypes);
        IGameViewable gameView = viewFactory.createEngineGameViewFor(viewOptions);

        GameModel gameModel = dataModelisationFactory.initGameDatas(playersTypes, playerViews);
        Optional<List<PlayerModel>> playerModels = dataModelisationFactory.getPlayerModels();

        viewFactory.setGameModelForModelAccesibilityViews(gameModel);

        if (playerModels.isEmpty()) throw new RuntimeException("Player models are not initialized");

        return new GameExecutor(
                new GameEngine(
                        gameModel,
                        gameActionsFactory.getGameActions(gameModel),
                        gameActionsFactory.getEndTurnActions(gameModel),
                        gameActionsFactory.getEndGameActions(gameModel, viewOptions.contains(ViewOptions.CLI_VERBOSE)),
                        playerFactory.createPlayers(
                                playersTypes,
                                playerModels.get(),
                                gameModel,
                                playerEngineViews
                                ),
                        gameView
                ),
                gameModel
        );
    }
}
