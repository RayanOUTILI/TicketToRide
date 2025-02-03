package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameStatisticsTest {

    private GameStatistics gameStatistics;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        gameStatistics = new GameStatistics();
        objectMapper = mock(ObjectMapper.class);
        gameStatistics.setObjectMapper(objectMapper);
    }

    @Test
    void readGameResultsSuccessfully() throws IOException {
        GameResultWrapper gameResultWrapper = new GameResultWrapper(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, 50, 3);
        List<GameResultWrapper> expectedResults = Arrays.asList(
                gameResultWrapper,
                new GameResultWrapper()
        );
        when(objectMapper.readValue(Mockito.any(File.class), Mockito.any(TypeReference.class)))
                .thenReturn(expectedResults);

        List<GameResultWrapper> actualResults = gameStatistics.readGameResults();
        assertEquals(PlayerIdentification.GREEN, gameResultWrapper.getWinner());
        assertEquals(PlayerType.MEDIUM_BOT, gameResultWrapper.getWinnerType());
        assertEquals(50, gameResultWrapper.getTotalTurns());
        assertEquals(3, gameResultWrapper.getNumberOfBots());
        assertEquals(expectedResults, actualResults);
    }

    @Test
    void readGameResultsFileNotFound() throws IOException {
        when(objectMapper.readValue(Mockito.any(File.class), Mockito.any(TypeReference.class)))
                .thenThrow(new IOException("File not found"));

        IOException exception = org.junit.jupiter.api.Assertions.assertThrows(IOException.class, () -> {
            gameStatistics.readGameResults();
        });

        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void calculateWinRatesWithValidResults() {
        GameResultWrapper gameResult1 = mock(GameResultWrapper.class);
        when(gameResult1.getWinnerType()).thenReturn(PlayerType.EASY_BOT);
        GameResultWrapper gameResult2 = mock(GameResultWrapper.class);
        when(gameResult2.getWinnerType()).thenReturn(PlayerType.EASY_BOT);
        GameResultWrapper gameResult3 = mock(GameResultWrapper.class);
        when(gameResult3.getWinnerType()).thenReturn(PlayerType.MEDIUM_BOT);

        List<GameResultWrapper> gameResults = Arrays.asList(
                gameResult1,
                gameResult2,
                gameResult3
        );

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        gameStatistics.calculateWinRates(gameResults);

        System.setOut(originalOut);
        String actualOutput = outContent.toString().replaceAll("[\\r\\n]", "");

        actualOutput = actualOutput.replaceAll(".*?(EASY_BOT win rate: \\d+,\\d+%).*", "$1");

        String expectedOutput = "EASY_BOT win rate: 66,67%";

        assertEquals(expectedOutput, actualOutput);
    }


    @Test
    void calculateWinRatesWithNoWinners() {
        List<GameResultWrapper> gameResults = Collections.emptyList();
        gameStatistics.calculateWinRates(gameResults);
        assertEquals(0, gameResults.size());
    }
}