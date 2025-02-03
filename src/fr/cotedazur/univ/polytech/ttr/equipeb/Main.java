package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Route> routes = (new MapFactory()).getMapFromJson();
            WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());

            DestinationCardsFactory destinationCardDeck = new DestinationCardsFactory();
            DestinationCardDeck<ShortDestinationCard> shortDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getShortDestinationCards());
            DestinationCardDeck<LongDestinationCard> longDestinationCardDeck = new DestinationCardDeck<>(destinationCardDeck.getLongDestinationCards());

            PlayerFactory playerFactory = new PlayerFactory();

            List<PlayerModel> playerModels = List.of(
                    new PlayerModel(PlayerIdentification.BLUE, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.BLUE)),
                    new PlayerModel(PlayerIdentification.RED, PlayerType.EASY_BOT, new PlayerConsoleView(PlayerIdentification.RED)),
                    new PlayerModel(PlayerIdentification.GREEN, PlayerType.MEDIUM_BOT, new PlayerConsoleView(PlayerIdentification.GREEN))
            );

            GameModel gameModel = new GameModel(playerModels, wagonCardDeck, shortDestinationCardDeck, longDestinationCardDeck, routes);
            List<Player> players = playerFactory.createTwoEasyOneMediumBots(playerModels, gameModel);

            GameEngine gameEngine = new GameEngine(gameModel, players);
            gameEngine.initGame();
            gameEngine.initPlayers();
            gameEngine.startGame();

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
