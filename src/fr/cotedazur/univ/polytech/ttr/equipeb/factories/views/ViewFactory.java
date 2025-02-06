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

    private StatsViewFactory statsViewFactory;
    private List<PlayerConsoleView> playerConsoleViews;

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

        if (viewOptions.contains(ViewOptions.CSV)){
            compositeGameEngineView.addView(statsViewFactory.createCSVViewForGameEngine());
        }

        if (viewOptions.contains(ViewOptions.CLI_VERBOSE)){
            compositeGameEngineView.addView(new GameConsoleView());
        }

        if (viewOptions.contains(ViewOptions.CLI_STATS)){
//            throw new UnsupportedOperationException("Not implemented yet");
        }

        return compositeGameEngineView;
    }

    public List<IPlayerEngineViewable> createPlayerEngineViewsFor(List<ViewOptions> viewOptions, List<PlayerType> playerTypes){
        List<CompositePlayerEngineView> compositePlayerEngineViews = playerTypes.stream()
                .map(playerModel -> new CompositePlayerEngineView()).toList();

        // CSV
        if (viewOptions.contains(ViewOptions.CSV)){
            List<PlayerStatisticsView> playerStatisticsViews = statsViewFactory.createCSVViewsForPlayers(playerTypes);
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

        // CLI_STATS
        if (viewOptions.contains(ViewOptions.CLI_STATS)){
            //throw new UnsupportedOperationException("Not implemented yet");
        }

        return new ArrayList<>(compositePlayerEngineViews);
    }

    public List<IPlayerViewable> createPlayerViewsFor(List<ViewOptions> viewOptions, List<PlayerType> playerTypes){
        List<CompositePlayerView> playerViews = playerTypes.stream()
                .map(playerModel -> new CompositePlayerView()).toList();

        // NOTHING TO DO WITH CSV

        // CLI_VERBOSE
        if (viewOptions.contains(ViewOptions.CLI_VERBOSE)){
            List<PlayerConsoleView> playerCLIViews = createOrGetConsoleView(playerTypes.size());
            for (int i = 0; i < playerTypes.size(); i++) {
                playerViews.get(i).addView(playerCLIViews.get(i));
            }
        }

        // CLI_STATS
        if (viewOptions.contains(ViewOptions.CLI_STATS)){
//            throw new UnsupportedOperationException("Not implemented yet");
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
