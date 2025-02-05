package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameSimulator;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.StatsWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.GameStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.views.PlayerStatisticsView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.GameConsoleView;
import fr.cotedazur.univ.polytech.ttr.equipeb.views.IGameViewable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {

    private static final String FILE_PATH = "resources/stats/gameResult.csv";

    public static void main(String[] args) {
        try {

            List<Route> routes = (new MapFactory()).getMapFromJson();
            WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());

            DestinationCardsFactory destinationCardDeck = new DestinationCardsFactory();
            DestinationCardDeck<DestinationCard> shortDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getShortDestinationCards());
            DestinationCardDeck<DestinationCard> longDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getLongDestinationCards());

            PlayerFactory playerFactory = new PlayerFactory();

            PlayerModel playerModelBlue = new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, null);
            PlayerModel playerModelRed = new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, null);
            PlayerModel playerModelGreen = new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, null);

            List<PlayerModel> playerModels = List.of(
                    playerModelBlue,
                    playerModelRed,
                    playerModelGreen
            );

            IGameViewable gameView = new GameConsoleView();

            //TODO: Find a proper way for the views here
            List<IPlayerEngineViewable> playerEngineViewables = List.of(
                    new PlayerConsoleView(PlayerIdentification.BLUE),
                    new PlayerConsoleView(PlayerIdentification.RED),
                    new PlayerConsoleView(PlayerIdentification.GREEN)
            );

            // TODO: FOR CSV
            StatsWriter statsWriter = new StatsWriter(FILE_PATH, PlayerStatsLine.headers, true);

            GameModel gameModel = new GameModel(playerModels, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);

            // TODO: ADD THE CONDITION AND READ OF ARGS
            if (true) {

                PlayerStatsLine statsLineBlue = new PlayerStatsLine(UUID.randomUUID(), PlayerIdentification.BLUE, PlayerType.EASY_BOT);
                PlayerStatsLine statsLineRed = new PlayerStatsLine(UUID.randomUUID(), PlayerIdentification.RED, PlayerType.EASY_BOT);
                PlayerStatsLine statsLineGreen = new PlayerStatsLine(UUID.randomUUID(), PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT);

                PlayerStatisticsView statViewBlue = new PlayerStatisticsView(statsLineBlue, statsWriter);
                PlayerStatisticsView statViewRed = new PlayerStatisticsView(statsLineRed, statsWriter);
                PlayerStatisticsView statViewGreen = new PlayerStatisticsView(statsLineGreen, statsWriter);

                gameView = new GameStatisticsView(List.of(statViewBlue, statViewRed, statViewGreen));

                statViewBlue.setPlayerModel(playerModelBlue);
                statViewBlue.setGameModel(gameModel);
                statViewRed.setPlayerModel(playerModelRed);
                statViewRed.setGameModel(gameModel);
                statViewGreen.setPlayerModel(playerModelGreen);
                statViewGreen.setGameModel(gameModel);

                playerEngineViewables = List.of(statViewBlue, statViewRed, statViewGreen);
            }

            List<Player> players = playerFactory.createTwoEasyOneMediumBots(playerModels, gameModel, playerEngineViewables);

            Map<Action, Controller> gameControllers = Map.of(
                    Action.PICK_WAGON_CARD, new WagonCardsController(gameModel),
                    Action.CLAIM_ROUTE, new RoutesController(gameModel),
                    Action.PICK_DESTINATION_CARDS, new DestinationCardsController(gameModel),
                    Action.PLACE_STATION, new StationController(gameModel)
            );

            List<Controller> endPlayerTurnControllers = List.of(
                    new CurrentPlayerScoreController(gameModel)
            );

            List<Controller> endGameControllers = List.of(
                    new ChooseRouteStationController(gameModel),
                    new EndGameScoreController(gameModel)
            );

            GameEngine gameEngine = new GameEngine(gameModel, gameControllers, endPlayerTurnControllers, endGameControllers, players, gameView);
            GameSimulator gameSimulator = new GameSimulator(gameEngine);
            gameSimulator.simulateGame(2000);

            statsWriter.close();

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
