package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation.DataModelisationFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.game_actions.GameActionsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.players.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.views.ViewOptions;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameExecutor;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameExecutorFactoryTest {

    @Mock
    private DataModelisationFactory dataModelisationFactory;
    @Mock
    private GameActionsFactory gameActionsFactory;
    @Mock
    private PlayerFactory playerFactory;
    @Mock
    private ViewFactory viewFactory;
    @Mock
    private IGameViewable gameView;
    @Mock
    private GameModel gameModel;
    @Mock
    private PlayerModel playerModel;
    @Mock
    private IPlayerEngineViewable playerEngineView;
    @Mock
    private IPlayerViewable playerView;
    @Mock
    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGameSuccessfully() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT);
        List<ViewOptions> viewOptions = List.of(ViewOptions.CLI_VERBOSE, ViewOptions.DATABASE);
        List<IPlayerEngineViewable> playerEngineViews = List.of(playerEngineView);
        List<IPlayerViewable> playerViews = List.of(playerView);
        List<PlayerModel> playerModels = List.of(playerModel);

        when(viewFactory.createPlayerEngineViewsFor(viewOptions, playerTypes)).thenReturn(playerEngineViews);
        when(viewFactory.createPlayerViewsFor(viewOptions, playerTypes)).thenReturn(playerViews);
        when(viewFactory.createEngineGameViewFor(viewOptions)).thenReturn(gameView);
        when(dataModelisationFactory.initGameDatas(playerTypes, playerViews)).thenReturn(gameModel);
        when(dataModelisationFactory.getPlayerModels()).thenReturn(Optional.of(playerModels));
        when(gameActionsFactory.getGameActions(gameModel)).thenReturn(Collections.emptyMap());
        when(gameActionsFactory.getEndTurnActions(gameModel)).thenReturn(Collections.emptyList());
        when(gameActionsFactory.getEndGameActions(gameModel)).thenReturn(Collections.emptyList());
        when(playerFactory.createPlayers(playerTypes, playerModels, gameModel, playerEngineViews)).thenReturn(Collections.emptyList());

        GameExecutor gameExecutor = GameExecutorFactory.createGame(
                dataModelisationFactory,
                gameActionsFactory,
                playerFactory,
                viewFactory,
                playerTypes,
                viewOptions
        );

        assertNotNull(gameExecutor);
    }

    @Test
    void createGameWithNoPlayerModelsThrowsException() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.EASY_BOT);
        List<ViewOptions> viewOptions = List.of(ViewOptions.CLI_VERBOSE, ViewOptions.CLI_VERBOSE);
        List<IPlayerEngineViewable> playerEngineViews = List.of(playerEngineView);
        List<IPlayerViewable> playerViews = List.of(playerView);

        when(viewFactory.createPlayerEngineViewsFor(viewOptions, playerTypes)).thenReturn(playerEngineViews);
        when(viewFactory.createPlayerViewsFor(viewOptions, playerTypes)).thenReturn(playerViews);
        when(viewFactory.createEngineGameViewFor(viewOptions)).thenReturn(gameView);
        when(dataModelisationFactory.initGameDatas(playerTypes, playerViews)).thenReturn(gameModel);
        when(dataModelisationFactory.getPlayerModels()).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> GameExecutorFactory.createGame(
                dataModelisationFactory,
                gameActionsFactory,
                playerFactory,
                viewFactory,
                playerTypes,
                viewOptions
        ));

        assertEquals("Player models are not initialized", exception.getMessage());
    }
}