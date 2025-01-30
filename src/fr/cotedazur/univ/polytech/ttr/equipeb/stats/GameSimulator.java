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
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameResult;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.List;

public class GameSimulator {

    private final GameResultPersistence gameResultPersistence;
    private final PlayerFactory playerFactory;
    private final List<Route> routes;
    private final WagonCardDeck wagonCardDeck;
    private final DestinationCardDeck destinationCardDeck;
    private List<PlayerModel> playerModels;

    public GameSimulator() throws JsonParseException {
        this.gameResultPersistence = new GameResultPersistence();
        this.playerFactory = new PlayerFactory();

        this.routes = (new MapFactory()).getMapFromJson();
        this.wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
        this.destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getShortDestinationCards());
    }

    /**
     * Simule une partie avec une liste de joueurs donnée.
     */
    public void simulateGame(List<Player> players) {
        GameModel gameModel = new GameModel(this.playerModels, wagonCardDeck, destinationCardDeck, routes);
        GameEngine gameEngine = new GameEngine(gameModel, players);
        int nbTurn = gameEngine.startGame();

        PlayerModel winner = gameModel.getWinner();
        if (winner != null) {
            GameResult gameResult = new GameResult(
                    winner.getIdentification(),
                    winner.getPlayerType(),
                    nbTurn,
                    gameModel.getNbOfPlayers()
            );
            gameResultPersistence.saveGameResult(gameResult);
        } else {
            System.out.println("No winner found.");
        }
    }

    public void simulateMultipleGames(int numSimulations, List<Player> players ) {
        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Simulating game " + (i + 1) + " of " + numSimulations);
            simulateGame(players);
        }
    }

    /**
     * Configuration 1 : 2 bots EASY + 1 bot MEDIUM
     */
    private List<Player> createEasyMediumConfig1() {
        this.playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        return playerFactory.createTwoEasyOneMediumBots(playerModels, new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes));
    }

    /**
     * Configuration 2 : 2 bots MEDIUM + 1 bot EASY
     */
    private List<Player> createEasyMediumConfig2() {
        this.playerModels = List.of(
                new PlayerModel(PlayerIdentification.BLUE, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                new PlayerModel(PlayerIdentification.RED, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                new PlayerModel(PlayerIdentification.GREEN, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
        );
        return playerFactory.createTwoMediumOneEasyBots(playerModels, new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes));
    }

    public static void main(String[] args) {
        try {
            GameSimulator simulator = new GameSimulator();
            simulator.simulateMultipleGames(5, simulator.createEasyMediumConfig1());
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
