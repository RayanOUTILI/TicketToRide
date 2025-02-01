package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameSimulatorTest {
    private GameSimulator gameSimulator;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        GameResultDataWriter gameResultPersistence = new GameResultDataWriter();
        PlayerFactory playerFactory = new PlayerFactory();
        gameSimulator = new GameSimulator();

        Field gameResultPersistenceField = GameSimulator.class.getDeclaredField("gameResultPersistence");
        gameResultPersistenceField.setAccessible(true);
        gameResultPersistenceField.set(gameSimulator, gameResultPersistence);


        Field playerFactoryField = GameSimulator.class.getDeclaredField("playerFactory");
        playerFactoryField.setAccessible(true);
        playerFactoryField.set(gameSimulator, playerFactory);
    }

    @Test
    void testGameModelCreation() throws JsonParseException {
        GameModel gameModel = gameSimulator.createNewGameModel(null);
        assertNotNull(gameModel);
    }

    @Test
    void testCreateEasyMediumConfig1() throws JsonParseException {
        Map<Player, PlayerModel> playersMap = gameSimulator.createEasyMediumConfig1();
        List<PlayerModel> playerModels = new ArrayList<>(playersMap.values());
        playerModels.sort(Comparator.comparing(PlayerModel::getPlayerType));
        assertEquals(3, playerModels.size());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(0).getPlayerType());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(1).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(2).getPlayerType());
    }

    @Test
    void testCreateEasyMediumConfig2() throws JsonParseException {
        Map<Player, PlayerModel> playersMap = gameSimulator.createEasyMediumConfig2();
        List<PlayerModel> playerModels = new ArrayList<>(playersMap.values());
        playerModels.sort(Comparator.comparing(PlayerModel::getPlayerType));
        assertEquals(3, playerModels.size());
        assertEquals(PlayerType.EASY_BOT, playerModels.get(0).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(1).getPlayerType());
        assertEquals(PlayerType.MEDIUM_BOT, playerModels.get(2).getPlayerType());
    }
}