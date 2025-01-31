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

public class GameSimulator {

    private final GameResultDataWriter gameResultPersistence;
    private final PlayerFactory playerFactory;
    List<PlayerModel> playerModels;

    public GameSimulator() {
        this.gameResultPersistence = new GameResultDataWriter();
        this.playerFactory = new PlayerFactory();
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
    protected void simulateGame(List<Player> players) throws JsonParseException {
        GameModel gameModel = createNewGameModel(playerModels);
        GameEngine gameEngine = new GameEngine(gameModel, players);
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
            System.out.println("Simulating game " + (i + 1) + " of " + numSimulations);
            simulateGame(createEasyMediumConfig1());
        }
    }

    /**
     * Configuration 1 : 2 bots EASY + 1 bot MEDIUM
     */
    public List<Player> createEasyMediumConfig1() throws JsonParseException {
        this.playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        return playerFactory.createTwoEasyOneMediumBots(playerModels, createNewGameModel(playerModels));
    }

    /**
     * Configuration 2 : 2 bots MEDIUM + 1 bot EASY
     */
    public List<Player> createEasyMediumConfig2() throws JsonParseException {
        List<PlayerModel> playersModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        return playerFactory.createTwoMediumOneEasyBots(playersModels, createNewGameModel(playerModels));
    }

    public static void main(String[] args) {
        try {
            GameSimulator simulator = new GameSimulator();
            simulator.simulateMultipleGames(1000);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
