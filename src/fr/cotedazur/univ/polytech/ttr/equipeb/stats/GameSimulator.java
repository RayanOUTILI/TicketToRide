package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GameSimulator {

    private final GameResultDataWriter gameResultPersistence;
    private final PlayerFactory playerFactory;
    private List<PlayerModel> playerModels;
    private GameModel gameModel;
    Logger logger;

    public GameSimulator() {
        this.gameResultPersistence = new GameResultDataWriter();
        this.playerFactory = new PlayerFactory();
        this.logger = Logger.getLogger(GameSimulator.class.getName());
        this.logger.setLevel(java.util.logging.Level.INFO);
    }

    public GameSimulator(GameResultDataWriter gameResultWrapper, PlayerFactory playerFactory) {
        this.gameResultPersistence = gameResultWrapper;
        this.playerFactory = playerFactory;
    }

    public GameSimulator(PlayerFactory mockPlayerFactory, GameResultDataWriter mockGameResultDataWriter) {
        this.gameResultPersistence = mockGameResultDataWriter;
        this.playerFactory = mockPlayerFactory;
    }

    /**
     * Initialize a new game model with the given players.
     */
    protected GameModel createNewGameModel(List<PlayerModel> playerModels) throws JsonParseException {
        List<Route> routes = (new MapFactory()).getMapFromJson();
        WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
        DestinationCardDeck destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getShortDestinationCards());
        return new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes);
    }

    /**
     * Simulate a game with the given players.
     */
    protected void simulateGame(Map<Player, PlayerModel> playersMap) throws JsonParseException {
        GameEngine gameEngine = new GameEngine(gameModel, List.copyOf(playersMap.keySet()));
        gameEngine.initGame();
        gameEngine.initPlayers();
        int nbTurn = gameEngine.startGame();

        PlayerModel winner = gameModel.getWinner();
        GameResultWrapper gameResult = new GameResultWrapper(
                winner.getIdentification(),
                winner.getPlayerType(),
                nbTurn,
                gameModel.getNbOfPlayers()
        );
        gameResultPersistence.saveGameResult(gameResult);
    }

    public void simulateMultipleGames(int numSimulations) throws JsonParseException {
        for (int i = 0; i < numSimulations; i++) {
            logger.info(String.format("Simulating game %d of %d", (i + 1), numSimulations));

            simulateGame(createEasyMediumConfig1());
        }
    }

    /**
     * Configuration 1 : 2 bots EASY + 1 bot MEDIUM
     */
    public Map<Player, PlayerModel> createEasyMediumConfig1() throws JsonParseException {
        this.playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        // Créer le GameModel avec la liste de PlayerModels
        gameModel = createNewGameModel(playerModels);

        // Créer les joueurs à partir du GameModel
        List<Player> players = playerFactory.createTwoEasyOneMediumBots(playerModels, gameModel);

        // Retourner la correspondance entre les joueurs et les modèles de joueurs
        return Map.of(
                players.get(0), playerModels.get(0),
                players.get(1), playerModels.get(1),
                players.get(2), playerModels.get(2)
        );
    }

    /**
     * Configuration 2 : 1 bots EASY + 2 bot MEDIUM
     */
    public Map<Player, PlayerModel> createEasyMediumConfig2() throws JsonParseException {
        this.playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        // Créer le GameModel avec la liste de PlayerModels
        gameModel = createNewGameModel(playerModels);

        // Créer les joueurs à partir du GameModel
        List<Player> players = playerFactory.createTwoMediumOneEasyBots(playerModels, gameModel);

        // Retourner la correspondance entre les joueurs et les modèles de joueurs
        return Map.of(
                players.get(0), playerModels.get(0),
                players.get(1), playerModels.get(1),
                players.get(2), playerModels.get(2)
        );
    }

    public static void main(String[] args) {
        try {
            GameSimulator simulator = new GameSimulator();
            simulator.simulateMultipleGames(20000);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
