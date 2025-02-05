package fr.cotedazur.univ.polytech.ttr.equipeb.stats.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.util.List;
import java.util.UUID;

public class GameStatisticsView implements IGameViewable {

    List<PlayerStatisticsView> playerStatisticsViews;

    public GameStatisticsView(List<PlayerStatisticsView> playerStatisticsViews) {
        this.playerStatisticsViews = playerStatisticsViews;
    }

    @Override
    public void displayNewGame() {
        UUID gameId = UUID.randomUUID();
        playerStatisticsViews.forEach(playerStatisticsView -> playerStatisticsView.displayNewGame(gameId));
    }

    @Override
    public void displayNewTurn(int currentTurn) {
        playerStatisticsViews.forEach(playerStatisticsView -> playerStatisticsView.displayNewTurn(currentTurn));
    }

    @Override
    public void displayEndGameReason(PlayerIdentification playerIdentification, int nbOfWagons, int nbTurns) {
        // NOT USED
    }

    @Override
    public void displayWinner(PlayerIdentification playerId, int score) {
        playerStatisticsViews.forEach(playerStatisticsView -> playerStatisticsView.displayWinner(playerId, score));
        playerStatisticsViews.forEach(PlayerStatisticsView::writeStats);
    }
}
