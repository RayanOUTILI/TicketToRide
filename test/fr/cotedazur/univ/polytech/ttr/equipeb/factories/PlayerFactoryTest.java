package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.factories.players.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerFactoryTest {

    @Mock
    private GameModel gameModel;
    @Mock
    private PlayerModel playerModel;
    @Mock
    private IPlayerEngineViewable playerEngineViewable;

    private PlayerFactory playerFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerFactory = new PlayerFactory();
    }

    @Test
    void createPlayersSuccessfully() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT, PlayerType.OBJECTIVE_BOT);
        List<PlayerModel> playerModels = List.of(playerModel, playerModel, playerModel);
        List<IPlayerEngineViewable> playerEngineViewables = List.of(playerEngineViewable, playerEngineViewable, playerEngineViewable);

        List<Player> players = playerFactory.createPlayers(playerTypes, playerModels, gameModel, playerEngineViewables);

        assertNotNull(players);
        assertEquals(3, players.size());
    }

    @Test
    void createPlayersWithMismatchedListsThrowsException() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT);
        List<PlayerModel> playerModels = List.of(playerModel);
        List<IPlayerEngineViewable> playerEngineViewables = List.of(playerEngineViewable, playerEngineViewable);

        assertThrows(IndexOutOfBoundsException.class, () -> playerFactory.createPlayers(playerTypes, playerModels, gameModel, playerEngineViewables));
    }

    @Test
    void createPlayerWithEasyBotEngine() {
        Player player = playerFactory.createPlayer(PlayerType.EASY_BOT, playerModel, gameModel, playerEngineViewable);

        assertNotNull(player);
    }

    @Test
    void createPlayerWithMediumBotEngine() {
        Player player = playerFactory.createPlayer(PlayerType.MEDIUM_BOT, playerModel, gameModel, playerEngineViewable);

        assertNotNull(player);
    }

    @Test
    void createPlayerWithObjectiveBotEngine() {
        Player player = playerFactory.createPlayer(PlayerType.OBJECTIVE_BOT, playerModel, gameModel, playerEngineViewable);

        assertNotNull(player);
    }
}