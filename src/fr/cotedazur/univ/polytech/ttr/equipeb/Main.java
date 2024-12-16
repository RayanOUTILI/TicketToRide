package fr.cotedazur.univ.polytech.ttr.equipeb;

import fr.cotedazur.univ.polytech.ttr.equipeb.controllers.GameEngine;
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

        GameEngine gameEngine = new GameEngine(gameModel);
        gameEngine.startGame(playerModel);
    }
}
