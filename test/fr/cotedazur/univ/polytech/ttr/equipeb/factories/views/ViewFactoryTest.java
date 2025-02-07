package fr.cotedazur.univ.polytech.ttr.equipeb.factories.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.CompositePlayerEngineView;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.CompositePlayerView;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.PlayerStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.CompositeGameEngineView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class ViewFactoryTest {

    private ViewFactory viewFactory;
    private StatsViewFactory statsViewFactory;

    @BeforeEach
    void setUp() {
        statsViewFactory = mock(StatsViewFactory.class);
        viewFactory = new ViewFactory(statsViewFactory);
    }

    @Test
    void testCreateEngineGameViewFor() {
        List<ViewOptions> viewOptions = List.of(ViewOptions.CSV, ViewOptions.CLI_VERBOSE);
        IGameViewable gameView = viewFactory.createEngineGameViewFor(viewOptions);

        assertInstanceOf(CompositeGameEngineView.class, gameView);
    }

    @Test
    void testCreatePlayerEngineViewsFor() {
        List<ViewOptions> viewOptions = List.of(ViewOptions.CSV, ViewOptions.CLI_VERBOSE);
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.EASY_BOT);

        when(statsViewFactory.createStatsViewsForPlayers(playerTypes)).thenReturn(List.of(mock(PlayerStatisticsView.class), mock(PlayerStatisticsView.class)));
        List<IPlayerEngineViewable> playerViews = viewFactory.createPlayerEngineViewsFor(viewOptions, playerTypes);

        assertEquals(2, playerViews.size());
        for (IPlayerEngineViewable view : playerViews) {
            assertInstanceOf(CompositePlayerEngineView.class, view);
        }
    }

    @Test
    void testCreatePlayerViewsFor() {
        List<ViewOptions> viewOptions = List.of(ViewOptions.CLI_VERBOSE);
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.EASY_BOT);

        List<IPlayerViewable> playerViews = viewFactory.createPlayerViewsFor(viewOptions, playerTypes);

        assertEquals(2, playerViews.size());
        for (IPlayerViewable view : playerViews) {
            assertInstanceOf(CompositePlayerView.class, view);
        }
    }

    @Test
    void testSetGameModelForModelAccesibilityViews() {
        GameModel gameModel = mock(GameModel.class);
        viewFactory.setGameModelForModelAccesibilityViews(gameModel);

        verify(statsViewFactory).setGameModel(gameModel);
    }
}