package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.console;

import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatAction;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatActionStatus;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.StatsWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConsoleStatsWriter extends StatsWriter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleStatsWriter.class);

    Map<UUID, PlayerStatsInfos> playerStatsInfos;
    Map<String, Integer> gameLabels;

    public ConsoleStatsWriter() {
        this.playerStatsInfos = new HashMap<>();
        this.gameLabels = new HashMap<>();
    }

    public void push() {
        super.getBuffer().forEach(playerStatsLine -> {
            PlayerStatsInfos infos = this.playerStatsInfos.computeIfAbsent(playerStatsLine.getPlayerId(), id ->
                    new PlayerStatsInfos(playerStatsLine.getPlayerType(), playerStatsLine.getPlayerId(), playerStatsLine.getPlayerColor(), playerStatsLine.getLabel())
            );

            if (playerStatsLine.getAction().equals(StatAction.WINNER)) {
                infos.addScore(playerStatsLine.getScore());
                if (playerStatsLine.getActionStatus().equals(StatActionStatus.YES)){
                    infos.incrementWinnedGames();
                    gameLabels.merge(playerStatsLine.getLabel(), 1, Integer::sum);
                }
            }
        });
        super.clearBuffer();
    }

    @Override
    public void close() throws Exception {
        logger.info("Console statistics:");
        for (Map.Entry<String, Integer> entry : gameLabels.entrySet()) {
            String gameLabel = entry.getKey();
            Integer playCount = entry.getValue();
            logger.info("Game {{}} played {} times", gameLabel, playCount);
            for (PlayerStatsInfos playerStatsInfo : this.playerStatsInfos.values()) {
                if (playerStatsInfo.getGameLabel().equals(gameLabel)) {
                    logger.info("Player {} ({}) won {} games ({}%%), games with average score of {}",
                            playerStatsInfo.getPlayerColor(),
                            playerStatsInfo.getPlayerType(),
                            playerStatsInfo.getWinnedGames(),
                            ((double) playerStatsInfo.getWinnedGames()) / playCount * 100,
                            playerStatsInfo.calculateAverageScore()
                    );
                }
            }
            logger.info("-------- --------------- --------");
        }
    }
}
