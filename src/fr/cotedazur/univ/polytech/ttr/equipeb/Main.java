package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.engine.GameEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.WagonCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PlayerModel playerModel = new PlayerModel(PlayerIdentification.DEFAULT);
        List<Route> routes = (new MapFactory()).getSmallMap();
        WagonCardDeck wagonCardDeck = new WagonCardDeck((new WagonCardsFactory()).getWagonCards());
        DestinationCardDeck destinationCardDeck = new DestinationCardDeck((new DestinationCardsFactory()).getDestinationCards());
        GameModel gameModel = new GameModel(List.of(playerModel), wagonCardDeck, destinationCardDeck, routes);

        MapFactory mapFactory = new MapFactory();
        List<Route> routesJson = mapFactory.getMapFromJson("data/routes-europe.json");
        routesJson.forEach(route -> System.out.println(route.getFirstCity().getName() + " - " + route.getSecondCity().getName() + " - " + route.getLength() + " - " + route.getType() + " - " + route.getColor() + " - " + route.getNbLocomotives()));

        GameEngine gameEngine = new GameEngine(gameModel);
        gameEngine.startGame(playerModel);
    }
}
