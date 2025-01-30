package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GameResultDataWriter {

    static ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "resources/stats/gameResult.json";

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
            System.out.println("Game result has been saved to JSON.");
        } catch (IOException e) {
            System.out.println("Error saving game result to JSON: " + e.getMessage());
        }
    }
}