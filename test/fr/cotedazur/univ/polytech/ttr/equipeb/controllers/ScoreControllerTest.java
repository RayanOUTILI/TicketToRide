package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IVictoryControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.*;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoreControllerTest {
    ScoreController scoreController;
    IScoreControllerGameModel gameModel;
    IPlayerModelControllable player;

    RouteReadOnly routeLength1;
    RouteReadOnly routeLength2;
    RouteReadOnly routeLength3;
    RouteReadOnly routeLength4;
    RouteReadOnly routeLength6;
    RouteReadOnly routeLength8;
    RouteReadOnly getRouteLengthOther;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IScoreControllerGameModel.class);
        scoreController = new ScoreController(gameModel);
        player = new PlayerModel(PlayerIdentification.BLACK);

        routeLength1 = mock(RouteReadOnly.class);
            when(routeLength1.getLength()).thenReturn(1);
            when(routeLength1.isClaimed()).thenReturn(true);
        routeLength2 = mock(RouteReadOnly.class);
            when(routeLength2.getLength()).thenReturn(2);
            when(routeLength2.isClaimed()).thenReturn(true);
        routeLength3 = mock(RouteReadOnly.class);
            when(routeLength3.getLength()).thenReturn(3);
            when(routeLength3.isClaimed()).thenReturn(true);
        routeLength4 = mock(RouteReadOnly.class);
            when(routeLength4.getLength()).thenReturn(4);
            when(routeLength4.isClaimed()).thenReturn(true);
        routeLength6 = mock(RouteReadOnly.class);
            when(routeLength6.getLength()).thenReturn(6);
            when(routeLength6.isClaimed()).thenReturn(true);
        routeLength8 = mock(RouteReadOnly.class);
            when(routeLength8.getLength()).thenReturn(8);
            when(routeLength8.isClaimed()).thenReturn(true);
        getRouteLengthOther = mock(RouteReadOnly.class);
            when(getRouteLengthOther.getLength()).thenReturn(5);
            when(getRouteLengthOther.isClaimed()).thenReturn(true);
    }

    @org.junit.jupiter.api.Test
    void testUpdateScoreWithMultipleRoutes() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1, routeLength2, routeLength3, routeLength4, routeLength6, routeLength8, getRouteLengthOther));
        scoreController.updateScore(player);
        assertEquals((1 + 2 + 4 + 7 + 15 + 21 + 0), player.getScore());
    }

    @org.junit.jupiter.api.Test
    void testUpdateScoreWithNoRoutes() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of());
        scoreController.updateScore(player);
        assertEquals(0, player.getScore());
    }

    @org.junit.jupiter.api.Test
    void testUpdateScoreWithOneRoute() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1));
        scoreController.updateScore(player);
        assertEquals(1, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength2));
        scoreController.updateScore(player);
        assertEquals(2, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength3));
        scoreController.updateScore(player);
        assertEquals(4, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength4));
        scoreController.updateScore(player);
        assertEquals(7, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength6));
        scoreController.updateScore(player);
        assertEquals(15, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength8));
        scoreController.updateScore(player);
        assertEquals(21, player.getScore());

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(getRouteLengthOther));
        scoreController.updateScore(player);
        assertEquals(0, player.getScore());
    }

    @Test
    void testCalculateDestinationsScores() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, RouteColor.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, RouteColor.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);


        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        scoreController.calculateFinalScores();
        assertEquals(10, playerWithDestinations.getScore());
    }

    @Test
    void testCalculateDestinationsScoresWithAnIncompleteObjective() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Berlin"), new City("Paris"), 10);
        ShortDestinationCard parisToDakarDestination = new ShortDestinationCard(new City("Paris"), new City("Dakar"), 5);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination, parisToDakarDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, RouteColor.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, RouteColor.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);


        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        scoreController.calculateFinalScores();
        assertEquals(5, playerWithDestinations.getScore());
    }

}
