package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.PlayerFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.PlayerConsoleView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Route> routes = (new MapFactory()).getMapFromJson();
            WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
            DestinationCardDeck destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getShortDestinationCards());

            PlayerFactory playerFactory = new PlayerFactory();

            List<PlayerModel> playerModels = List.of(
                    new PlayerModel(PlayerIdentification.BLUE, new PlayerConsoleView(PlayerIdentification.BLUE)),
                    new PlayerModel(PlayerIdentification.RED, new PlayerConsoleView(PlayerIdentification.RED)),
                    new PlayerModel(PlayerIdentification.GREEN, new PlayerConsoleView(PlayerIdentification.GREEN))
            );

            GameModel gameModel = new GameModel(playerModels, wagonCardDeck, destinationCardDeck, routes);
            List<Player> players = playerFactory.createThreeEasyBots(playerModels, gameModel);

            GameEngine gameEngine = new GameEngine(gameModel, players);
            gameEngine.startGame();

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}
