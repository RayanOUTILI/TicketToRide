package fr.cotedazur.univ.polytech.ttr.equipeb.factories.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.PlayerStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.CompositeGameEngineView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.ArrayList;
import java.util.List;

public class ViewFactory {

    private final StatsViewFactory statsViewFactory;
    private final List<PlayerConsoleView> playerConsoleViews;

    public ViewFactory(StatsViewFactory statsViewFactory){
        this.statsViewFactory = statsViewFactory;
        this.playerConsoleViews = new ArrayList<>();
    }

    /**
     * <p>
     * <b>Important:</b> This method can be called only after the createPlayerEngineViewsFor.
     * </p>
     * @return Game Engine Views
     */
    public IGameViewable createEngineGameViewFor(List<ViewOptions> viewOptions){
        CompositeGameEngineView compositeGameEngineView = new CompositeGameEngineView();

        if (viewOptions.contains(ViewOptions.CSV)
                || viewOptions.contains(ViewOptions.DATABASE)
                || viewOptions.contains(ViewOptions.CLI_STATS)){
            compositeGameEngineView.addView(statsViewFactory.createStatsViewForGameEngine());
        }

        if (viewOptions.contains(ViewOptions.CLI_VERBOSE)){
            compositeGameEngineView.addView(new GameConsoleView());
        }

        return compositeGameEngineView;
    }

    public List<IPlayerEngineViewable> createPlayerEngineViewsFor(List<ViewOptions> viewOptions, List<PlayerType> playerTypes){
        List<CompositePlayerEngineView> compositePlayerEngineViews = playerTypes.stream()
                .map(playerModel -> new CompositePlayerEngineView()).toList();

        // CSV
        if (viewOptions.contains(ViewOptions.CSV)
                || viewOptions.contains(ViewOptions.DATABASE)
                || viewOptions.contains(ViewOptions.CLI_STATS)){
            List<PlayerStatisticsView> playerStatisticsViews = statsViewFactory.createStatsViewsForPlayers(playerTypes);
            for (int i = 0; i < playerTypes.size(); i++) {
                compositePlayerEngineViews.get(i).addView(playerStatisticsViews.get(i));
            }
        }

        // CLI_VERBOSE
        if (viewOptions.contains(ViewOptions.CLI_VERBOSE)){
            List<PlayerConsoleView> playerCLIViews = createOrGetConsoleView(playerTypes.size());
            for (int i = 0; i < playerTypes.size(); i++) {
                compositePlayerEngineViews.get(i).addView(playerCLIViews.get(i));
            }
        }

        return new ArrayList<>(compositePlayerEngineViews);
    }

    public List<IPlayerViewable> createPlayerViewsFor(List<ViewOptions> viewOptions, List<PlayerType> playerTypes){
        List<CompositePlayerView> playerViews = playerTypes.stream()
                .map(playerModel -> new CompositePlayerView()).toList();

        // CLI_VERBOSE
        if (viewOptions.contains(ViewOptions.CLI_VERBOSE)){
            List<PlayerConsoleView> playerCLIViews = createOrGetConsoleView(playerTypes.size());
            for (int i = 0; i < playerTypes.size(); i++) {
                playerViews.get(i).addView(playerCLIViews.get(i));
            }
        }

        return new ArrayList<>(playerViews);
    }



    private List<PlayerConsoleView> createOrGetConsoleView(int size){
        if (playerConsoleViews.isEmpty()){
            for (int i = 0; i < size; i++) {
                playerConsoleViews.add(new PlayerConsoleView());
            }
        }
        return playerConsoleViews;
    }

    public void setGameModelForModelAccesibilityViews(GameModel gameModel){
        statsViewFactory.setGameModel(gameModel);
    }


}
