package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class that analyzes game statistics.
 * It reads game results from a JSON file and calculates win rates for different player types.
 */
public class GameStatistics {

    private static final String FILE_PATH = "resources/stats/gameResult.json";
    private final Logger logger = Logger.getLogger(GameStatistics.class.getName());

    private ObjectMapper objectMapper = new ObjectMapper();

    public GameStatistics() {
        this.logger.setLevel(Level.ALL);
    }

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

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);

        winCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(
                        (double) entry2.getValue() / totalGames * 100,
                        (double) entry1.getValue() / totalGames * 100))
                .forEach(entry -> {
                    double winRate = (double) entry.getValue() / totalGames * 100;
                    System.out.println(entry.getKey() + " win rate: " + decimalFormat.format(winRate) + "%");
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
            stats.logger.log(Level.SEVERE, "Error reading game results from JSON file", e);
            e.printStackTrace();
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
