package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameResultDataWriterTest {
    private GameResultDataWriter gameResultDataWriter;
    private GameResultWrapper gameResult;

    @BeforeEach
    void setUp() {
        gameResultDataWriter = new GameResultDataWriter(false);
        gameResult = new GameResultWrapper();
    }

    @Test
    void testSaveGameResult() {
        gameResultDataWriter.saveGameResult(gameResult);
        File file = new File("resources/stats/gameResult.json");

        assertTrue(file.exists());

        if(file.exists()) {
            assertTrue(file.delete());
        }
    }


}