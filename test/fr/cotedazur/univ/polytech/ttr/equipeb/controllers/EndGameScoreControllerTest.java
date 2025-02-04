package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EndGameScoreControllerTest {
    EndGameScoreController endGameScoreController;
    IScoreControllerGameModel gameModel;
    Player player;

    @BeforeEach
    void setUp() {
        gameModel = mock(IScoreControllerGameModel.class);
        endGameScoreController = new EndGameScoreController(gameModel);
        player = mock(Player.class);
        when(player.clearScore()).thenReturn(true);
        assertTrue(endGameScoreController.initGame());
        assertTrue(endGameScoreController.initPlayer(player));
    }

    @AfterEach
    void tearDown() {
        assertTrue(endGameScoreController.resetPlayer(player));
        assertTrue(endGameScoreController.resetGame());
    }

    @Test
    void testCalculateFinalScoreWithLongestRouteLooping() {
        City danzic = new City("Danzic");
        City petrograd = new City("Petrograd");
        City riga = new City("Riga");
        City berlin = new City("Berlin");

        PlayerModel playerWithLoopingRoute = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithLoopingRoute.defineStartingStationsNumber(3);
        Route routeMunchenToWien = new Route(new City("Munchen"), new City("Wien"), 3, RouteType.TRAIN, Color.BLACK, 5);
        routeMunchenToWien.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeWienToBerlin = new Route(new City("Wien"), new City("Berlin"), 3, RouteType.TRAIN, Color.BLACK, 5);
        routeWienToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeBerlinToMunchen = new Route(berlin, new City("Munchen"), 2, RouteType.TRAIN, Color.BLACK, 5);
        routeBerlinToMunchen.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeMunchenToZurich = new Route(new City("Munchen"), new City("Zurich"), 4, RouteType.TRAIN, Color.BLACK, 5);
        routeMunchenToZurich.setClaimerPlayer(PlayerIdentification.BLACK);

        PlayerModel otherPlayer = new PlayerModel(PlayerIdentification.GREEN, null);
        otherPlayer.defineStartingStationsNumber(3);
        Route routeDanzicToRiga = new Route(danzic, riga, 6, RouteType.TRAIN, Color.BLACK, 5);
        routeDanzicToRiga.setClaimerPlayer(PlayerIdentification.GREEN);
        Route routeRigaToPetrograd = new Route(riga, petrograd, 4, RouteType.TRAIN, Color.BLACK, 5);
        routeRigaToPetrograd.setClaimerPlayer(PlayerIdentification.GREEN);

        ShortDestinationCard danzicToPetrograd = new ShortDestinationCard(danzic, petrograd, 10);
        ShortDestinationCard rigaToBerlin = new ShortDestinationCard(riga, berlin, 5);
        Player playerWithLoop = new Player(null, playerWithLoopingRoute);
        playerWithLoop.receivedDestinationCards(List.of(rigaToBerlin));

        Player otherPlayerExcluded = new Player(null, otherPlayer);
        otherPlayerExcluded.receivedDestinationCards(List.of(danzicToPetrograd));

        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMunchenToWien, routeWienToBerlin, routeBerlinToMunchen, routeMunchenToZurich));
        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.GREEN)).thenReturn(List.of(routeDanzicToRiga, routeRigaToPetrograd));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithLoopingRoute, otherPlayer));

        endGameScoreController.doAction(playerWithLoop);
        endGameScoreController.doAction(otherPlayerExcluded);
        assertEquals(17, playerWithLoopingRoute.getScore());
        assertEquals(22, otherPlayerExcluded.getScore());
    }
}
