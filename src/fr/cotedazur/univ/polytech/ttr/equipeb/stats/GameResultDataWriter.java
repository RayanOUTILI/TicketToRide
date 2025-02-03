package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameResultDataWriter {
    private final Logger logger = LoggerFactory.getLogger(GameResultDataWriter.class);
    private static final String FILE_PATH = "resources/stats/gameResult.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameResultDataWriter() {
    }

    /**
     * Enregistre un résultat de jeu dans le fichier JSON.
     * @param gameResult Résultat à enregistrer.
     */
    public void saveGameResult(GameResultWrapper gameResult) {
        File file = new File(FILE_PATH);

        try {
            List<GameResultWrapper> gameResults = new ArrayList<>();

            if (file.exists() && file.length() > 0) {
                try {
                    gameResults = objectMapper.readValue(file, new TypeReference<List<GameResultWrapper>>() {});
                } catch (IOException e) {
                    GameResultWrapper singleResult = objectMapper.readValue(file, GameResultWrapper.class);
                    gameResults.add(singleResult);
                }
            }
            gameResults.add(gameResult);
            objectMapper.writeValue(file, gameResults);
            logger.info("Game result has been successfully saved to JSON.");
        } catch (IOException e) {
            logger.error("Error saving game result to JSON: {}", e.getMessage());
        }
    }
}