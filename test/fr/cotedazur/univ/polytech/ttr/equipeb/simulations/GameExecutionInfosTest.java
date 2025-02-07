package fr.cotedazur.univ.polytech.ttr.equipeb.simulations;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameExecutionInfosTest {
    private GameExecutionInfos gameExecutionInfos;
    private List<PlayerType> players;

    @BeforeEach
    void setup() {
        players = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT);
        gameExecutionInfos = new GameExecutionInfos(players, 1);
    }

    @Test
    void testGetLabelWithDefaultLabel() {
        String label = gameExecutionInfos.getLabel();
        assertEquals("EASY_BOT - MEDIUM_BOT", label);
    }

    @Test
    void testGetLabelWithCustomLabel() {
        String newLabel = "Custom Game";
        gameExecutionInfos.setCustomLabel(newLabel);
        String label = gameExecutionInfos.getLabel();
        assertEquals("Custom Game", label);
    }

    @Test
    void testGetLabelWithEmptyPlayers() {
        GameExecutionInfos emptyGameExecutionInfos = new GameExecutionInfos(Collections.emptyList(), 1);
        String label = emptyGameExecutionInfos.getLabel();
        assertEquals("", label);
    }

    @Test
    void testGetPlayersType() {
        List<PlayerType> result = gameExecutionInfos.getPlayersType();
        assertEquals(players, result);
    }

    @Test
    void testGetExecutionNumber() {
        int executionNumber = gameExecutionInfos.getExecutionNumber();
        assertEquals(1, executionNumber);
    }

    @Test
    void testSetCustomLabelToNull() {
        gameExecutionInfos.setCustomLabel(null);
        String label = gameExecutionInfos.getLabel();
        assertEquals("EASY_BOT - MEDIUM_BOT", label);
    }

    @Test
    void testSetCustomLabelToEmptyString() {
        gameExecutionInfos.setCustomLabel("");
        String label = gameExecutionInfos.getLabel();
        assertEquals("", label);
    }
}