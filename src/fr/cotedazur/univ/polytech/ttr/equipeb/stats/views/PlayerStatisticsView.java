package fr.cotedazur.univ.polytech.ttr.equipeb.stats.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IStatsGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelStats;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.StatsWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatAction;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatActionStatus;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerStatisticsView implements IPlayerEngineViewable {

    private PlayerStatsLine statsLine;
    private IPlayerModelStats playerModel;
    private IStatsGameModel gameModel;
    private StatsWriter statsWriter;

    public PlayerStatisticsView(PlayerStatsLine statsLine, StatsWriter playerWriter) {
        this.statsWriter = playerWriter;
        this.statsLine = statsLine;
    }

    public void setPlayerModel(IPlayerModelStats playerModel) {
        this.playerModel = playerModel;
    }

    public void setGameModel(IStatsGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void writeStats(){
        statsWriter.push();
    }

    private void commitLine() {
        this.statsLine.setScore(playerModel.getScore());
        this.statsLine.setWagonsCards(playerModel.getNumberOfWagonCards());
        this.statsLine.setDestinationCards(playerModel.getDestinationCards().size());
        this.statsLine.setCurrentDestinationScore(calculateDestinationCardsScore());

        statsLine.setCurrentTime(System.nanoTime());
        statsWriter.commit(statsLine.getValues());
        statsLine = statsLine.cloneWithTurn();
    }

    private int calculateDestinationCardsScore() {
        List<RouteReadOnly> routes = new ArrayList<>();
        routes.addAll(playerModel.getSelectedStationRoutes());
        routes.addAll(gameModel.getAllRoutesClaimedByPlayer(playerModel.getIdentification()));
        Set<CityPair> allCityPairs = CitiesGraphUtils.findLengthBetweenAllCityInGraph(CitiesGraphUtils.getGraphFromRoutes(routes));

        List<DestinationCard> destinationCards = playerModel.getDestinationCards();


        return destinationCards.stream()
                .mapToInt(card -> {
                    CityPair pair = new CityPair(card.getStartCity(), card.getEndCity());
                    return allCityPairs.contains(pair) ? card.getPoints() : -card.getPoints();
                })
                .sum();
    }

    public void displayNewGame(UUID gameId) {
        this.statsLine.setGameId(gameId);
    }

    public void displayNewTurn(int currentTurn) {
        statsLine.setCurrentTurn(currentTurn);
    }

    public void displayActionRefused(Action action, ReasonActionRefused reason) {
        statsLine.setAction(StatAction.valueOf(action.name()));
        statsLine.setActionStatus(StatActionStatus.valueOf(reason.name()));
        commitLine();
    }

    public void displayActionCompleted(Action action) {
        statsLine.setAction(StatAction.valueOf(action.name()));
        statsLine.setActionStatus(StatActionStatus.YES);
        commitLine();
    }

    @Override
    public void displayActionStop() {
        statsLine.setAction(StatAction.STOP);
        statsLine.setActionStatus(StatActionStatus.YES);
        commitLine();
    }

/*    public void displayEndGameReason(PlayerIdentification playerIdentification, int nbOfWagons, int nbTurns) {
        statsLine.setCurrentTurn(nbTurns);
        statsLine.setAction(StatAction.END_GAME);
        if (playerIdentification.equals(playerModel.getPlayerIdentification())) {
            statsLine.setActionStatus(StatActionStatus.YES);
        } else {
            statsLine.setActionStatus(StatActionStatus.NO);
        }
        commitLine();
    }*/

    public void displayWinner(PlayerIdentification playerId, int score) {
        statsLine.setAction(StatAction.WINNER);
        if (playerId.equals(playerModel.getIdentification())) {
            statsLine.setActionStatus(StatActionStatus.YES);
        } else {
            statsLine.setActionStatus(StatActionStatus.NO);
        }
        commitLine();
    }
}
