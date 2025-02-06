package fr.cotedazur.univ.polytech.ttr.equipeb.factories.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.StatsWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.GameStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.PlayerStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatsViewFactory {
    private StatsWriter statsWriter;
    private List<PlayerStatisticsView> statisticsViews;
    private String label;

    public StatsViewFactory(StatsWriter statsWriter, String label){
        this.statsWriter = statsWriter;
        this.statisticsViews = new ArrayList<>();
        this.label = label;
    }

    public List<PlayerStatisticsView> createCSVViewsForPlayers(List<PlayerType> playerTypes){
        this.statisticsViews = new ArrayList<>();
        for (int i = 0; i < playerTypes.size(); i++) {
            statisticsViews.add(
                    new PlayerStatisticsView(
                            new PlayerStatsLine(
                                    UUID.randomUUID(),
                                    PlayerIdentification.values()[i],
                                    playerTypes.get(i),
                                    this.label
                            ),
                            statsWriter
                    )
            );
        }
        return statisticsViews;
    }

    public void setGameModel(GameModel model){
        statisticsViews.forEach(v -> v.setGameModel(model));
    }

    /**
     * <p>
     * <b>Important:</b> This method can be called only after the createCSVViews.
     * </p>
     *  @return GameView for CSV
     */
    public IGameViewable createCSVViewForGameEngine(){
        return new GameStatisticsView(statisticsViews);
    }
}
