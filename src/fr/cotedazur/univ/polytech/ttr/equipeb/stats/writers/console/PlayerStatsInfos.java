package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.console;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStatsInfos {

    private String gameLabel;

    private PlayerType playerType;
    private UUID playerId;
    private PlayerIdentification playerColor;

    private List<Integer> scores;
    private int winnedGames;

    public PlayerStatsInfos(PlayerType playerType, UUID playerId, PlayerIdentification playerColor, String gameLabel) {
        this.playerType = playerType;
        this.playerId = playerId;
        this.playerColor = playerColor;
        this.gameLabel = gameLabel;
        this.scores = new ArrayList<>();
        this.winnedGames = 0;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public PlayerIdentification getPlayerColor() {
        return playerColor;
    }

    public String getGameLabel() {
        return gameLabel;
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public void incrementWinnedGames() {
        winnedGames++;
    }

    public double calculateAverageScore() {
        return scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public int getWinnedGames() {
        return winnedGames;
    }
}
