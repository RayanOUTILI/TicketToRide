package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;

class GameSimulatorTest {

    private GameResultDataWriter gameResultPersistence;
    private PlayerFactory playerFactory;
    private GameSimulator gameSimulator;

    @BeforeEach
    void setUp() {
        gameResultPersistence = new GameResultDataWriter();
        playerFactory = new PlayerFactory();
        gameSimulator = new GameSimulator(gameResultPersistence, playerFactory);
    }

    @Test
    void testGameModelCreation() throws JsonParseException {
        GameModel gameModel = gameSimulator.createNewGameModel(null);
        assertTrue(gameModel != null);
    }

    @Test
    void testcreateEasyMediumConfig1() throws JsonParseException {
        Map<Player, PlayerModel> playersMap = gameSimulator.createEasyMediumConfig1();
        List<PlayerModel> playerModels = new ArrayList<>(playersMap.values());
        playerModels.sort(Comparator.comparing(PlayerModel::getPlayerType));
        assertEquals(3, playerModels.size());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(0).getPlayerType());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(1).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(2).getPlayerType());
    }

    @Test
    void testcreateEasyMediumConfig2() throws JsonParseException {
        Map<Player, PlayerModel> playersMap = gameSimulator.createEasyMediumConfig2();
        List<PlayerModel> playerModels = new ArrayList<>(playersMap.values());
        playerModels.sort(Comparator.comparing(PlayerModel::getPlayerType));
        assertEquals(3, playerModels.size());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(0).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(1).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(2).getPlayerType());
    }


    /*
    @Test
    void simulateGame_withWinner() throws JsonParseException {
        GameModel gameModel = mock(GameModel.class);
        PlayerModel winner = mock(PlayerModel.class);
        GameEngine gameEngine = mock(GameEngine.class);
        when(gameModel.getWinner()).thenReturn(winner);
        when(winner.getIdentification()).thenReturn(PlayerIdentification.BLUE);
        when(winner.getPlayerType()).thenReturn(PlayerType.EASY_BOT);
        when(gameModel.getNbOfPlayers()).thenReturn(3);

        List<PlayerModel> playerModels = List.of(mock(PlayerModel.class), mock(PlayerModel.class), mock(PlayerModel.class));
        when(playerFactory.createThreeEasyBots(anyList(), any(GameModel.class))).thenReturn(List.of(mock(Player.class), mock(Player.class), mock(Player.class)));
        GameSimulator spySimulator = spy(gameSimulator);
        when(spySimulator.createNewGameModel(playerModels)).thenReturn(gameModel);
        when(playerModels.get(0).getIdentification()).thenReturn(PlayerIdentification.RED);
        when(playerModels.get(1).getIdentification()).thenReturn(PlayerIdentification.GREEN);
        when(playerModels.get(2).getIdentification()).thenReturn(PlayerIdentification.BLUE);
        spySimulator.simulateGame(List.of(mock(Player.class)));


        List<Player> players = List.of(mock(Player.class), mock(Player.class), mock(Player.class));
        verify(gameResultPersistence).saveGameResult(any(GameResultWrapper.class));
    }*/

    /*@Test
    void simulateGame_noWinner() throws JsonParseException {
        GameModel gameModel = mock(GameModel.class);
        when(gameModel.getWinner()).thenReturn(null);

        List<PlayerModel> playerModels = List.of(mock(PlayerModel.class));
        when(playerFactory.createThreeEasyBots(anyList(), any(GameModel.class))).thenReturn(List.of(mock(Player.class), mock(Player.class), mock(Player.class)));
        doReturn(gameModel).when(gameSimulator).createNewGameModel(playerModels);

        gameSimulator.simulateGame(List.of(mock(Player.class)));

        verify(gameResultPersistence, never()).saveGameResult(any(GameResultWrapper.class));
    }

    @Test
    void simulateMultipleGames_runsCorrectNumberOfSimulations() throws JsonParseException {
        GameSimulator spySimulator = spy(gameSimulator);
        doNothing().when(spySimulator).simulateGame(anyList());

        spySimulator.simulateMultipleGames(5);

        verify(spySimulator, times(5)).simulateGame(anyList());
    }*/
}