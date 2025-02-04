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
import fr.cotedazur.univ.polytech.ttr.equipeb.simulations.GameSimulator;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            List<Route> routes = (new MapFactory()).getMapFromJson();
            WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());

            DestinationCardsFactory destinationCardDeck = new DestinationCardsFactory();
            DestinationCardDeck<DestinationCard> shortDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getShortDestinationCards());
            DestinationCardDeck<DestinationCard> longDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getLongDestinationCards());

            PlayerFactory playerFactory = new PlayerFactory();

            List<PlayerModel> playerModels = List.of(
                    new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, null /*new PlayerConsoleView(PlayerIdentification.BLUE)*/),
                    new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, null /* new PlayerConsoleView(PlayerIdentification.RED)*/),
                    new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, null /*new PlayerConsoleView(PlayerIdentification.GREEN)*/)
            );

            GameModel gameModel = new GameModel(playerModels, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);
            List<Player> players = playerFactory.createThreeEasyBotsWithoutViews(playerModels, gameModel);

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

            GameEngine gameEngine = new GameEngine(gameModel, gameControllers, endPlayerTurnControllers, endGameControllers, players);
            GameSimulator gameSimulator = new GameSimulator(gameEngine);
            gameSimulator.simulateGame(1000);

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
