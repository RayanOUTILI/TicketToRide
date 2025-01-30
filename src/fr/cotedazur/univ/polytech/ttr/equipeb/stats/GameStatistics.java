package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that analyzes game statistics.
 * It reads game results from a JSON file and calculates win rates for different player types.
 */
public class GameStatistics {

    private static final String FILE_PATH = "resources/stats/gameResult.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads and loads the game results from a JSON file.
     *
     * @return a list of {@link GameResultWrapper} objects representing the game results.
     * @throws IOException if there is an error reading the JSON file.
     */
    public List<GameResultWrapper> readGameResults() throws IOException {
        File file = new File(FILE_PATH);
        return objectMapper.readValue(file, new TypeReference<List<GameResultWrapper>>() {});
    }

    /**
     * Calculates and displays the win rates for each player type.
     *
     * @param gameResults the list of game results to analyze.
     */
    public void calculateWinRates(List<GameResultWrapper> gameResults) {
        Map<PlayerType, Long> winCounts = gameResults.stream()
                .filter(result -> result.getWinnerType() != null)
                .collect(Collectors.groupingBy(GameResultWrapper::getWinnerType, Collectors.counting()));

        long totalGames = gameResults.size();

        winCounts.forEach((playerType, count) -> {
            double winRate = (double) count / totalGames * 100;
            System.out.println(playerType + " win rate: " + String.format("%.2f", winRate) + "%");
        });
    }

    /**
     * Main entry point of the application to generate game statistics.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        GameStatistics stats = new GameStatistics();
        try {
            List<GameResultWrapper> gameResults = stats.readGameResults();
            stats.calculateWinRates(gameResults);
        } catch (IOException e) {
            System.err.println("Error reading game results: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
