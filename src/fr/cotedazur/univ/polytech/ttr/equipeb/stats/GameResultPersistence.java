package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameResult;

import java.io.File;
import java.io.IOException;

public class GameResultPersistence {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void saveGameResult(GameResult gameResult) {
        try {
            objectMapper.writeValue(new File("resources/stats", "gameResult.json"), gameResult);
            System.out.println("Game result has been saved to JSON.");
        } catch (IOException e) {
            System.out.println("Error saving game result to JSON: " + e.getMessage());
        }
    }
}
