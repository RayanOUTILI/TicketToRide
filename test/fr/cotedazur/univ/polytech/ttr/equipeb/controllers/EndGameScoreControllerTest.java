package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

class EndGameScoreControllerTest {
    EndGameScoreController endGameScoreController;
    IScoreControllerGameModel gameModel;
    Player player;

    Route routeLength1;
    Route routeLength2;
    Route routeLength3;
    Route routeLength4;
    Route routeLength6;
    Route routeLength8;
    Route getRouteLengthOther;

    /*@org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IScoreControllerGameModel.class);
        endGameScoreController = new EndGameScoreController(gameModel);
        player = new Player(null, new PlayerModel(PlayerIdentification.BLACK, null));

        routeLength1 = new Route(new City("Paris"), new City("Berlin"), 1, RouteType.TRAIN, Color.BLACK, 0);
        routeLength1.setClaimerPlayer(player.getIdentification());

        routeLength2 = new Route(new City("Paris"), new City("Berlin"), 2, RouteType.TRAIN, Color.BLACK, 0);
        routeLength2.setClaimerPlayer(player.getIdentification());

        routeLength3 = new Route(new City("Paris"), new City("Berlin"), 3, RouteType.TRAIN, Color.BLACK, 0);
        routeLength3.setClaimerPlayer(player.getIdentification());

        routeLength4 = new Route(new City("Paris"), new City("Berlin"), 4, RouteType.TRAIN, Color.BLACK, 0);
        routeLength4.setClaimerPlayer(player.getIdentification());

        routeLength6 = new Route(new City("Paris"), new City("Berlin"), 6, RouteType.TRAIN, Color.BLACK, 0);
        routeLength6.setClaimerPlayer(player.getIdentification());

        routeLength8 = new Route(new City("Paris"), new City("Berlin"), 8, RouteType.TRAIN, Color.BLACK, 0);
        routeLength8.setClaimerPlayer(player.getIdentification());

        getRouteLengthOther = new Route(new City("Paris"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 0);
        getRouteLengthOther.setClaimerPlayer(player.getIdentification());
    }

    @org.junit.jupiter.api.Test
    void testCalculatePlacedRoutesScoreWithMultipleRoutes() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1, routeLength2, routeLength3, routeLength4, routeLength6, routeLength8, getRouteLengthOther));
        when(gameModel.getPlayers()).thenReturn(List.of(player));
        endGameScoreController.doAction(player);
        // + 10 comes from longest Route
        assertEquals((1 + 2 + 4 + 7 + 15 + 21 + 0 +10), player.getScore());
    }

    @org.junit.jupiter.api.Test
    void testCalculatePlacedRoutesScoreWithNoRoutes() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of());
        endGameScoreController.doAction(player);
        assertEquals(0, player.getScore());
    }

    @org.junit.jupiter.api.Test
    void testCalculatePlacedRoutesScoreWithOneRoute() {
        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1));
        endGameScoreController.doAction(player);
        assertEquals(1, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength2));
        endGameScoreController.doAction(player);
        assertEquals(2, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength3));
        assertEquals(4, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength4));
        assertEquals(7, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength6));
        assertEquals(15, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength8));
        assertEquals(21, endGameScoreController.calculatePlacedRoutesScore(player));

        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(getRouteLengthOther));
        assertEquals(0, endGameScoreController.calculatePlacedRoutesScore(player));
    }

    @Test
    void testCalculateDestinationsScores() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);


        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        endGameScoreController.setFinalScores();
        // +10 for longest route
        assertEquals(10 + 10, playerWithDestinations.getScore());
    }

    @Test
    void testCalculateDestinationsScoresWithAnIncompleteObjective() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Berlin"), new City("Paris"), 10);
        ShortDestinationCard parisToDakarDestination = new ShortDestinationCard(new City("Paris"), new City("Dakar"), 5);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination, parisToDakarDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);


        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        endGameScoreController.setFinalScores();
        // +10 for longest route
        assertEquals(5 + 10, playerWithDestinations.getScore());
    }

    @Test
    void testCyclicDestinationsScores() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeBerlinToParis = new Route(new City("Berlin"), new City("Paris"), 0, RouteType.TRAIN, Color.BLACK, 5);
        routeBerlinToParis.setClaimerPlayer(PlayerIdentification.BLACK);


        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid, routeBerlinToParis));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        endGameScoreController.setFinalScores();
        // +10 for longest route
        assertEquals(10 + 10, playerWithDestinations.getScore());
    }


    @Test
    void testCalculateFinalScoreWithAStation() {
        player.defineStartingStationsNumber(3);
        when(gameModel.getPlayers()).thenReturn(List.of(player));
        endGameScoreController.setFinalScores();
        // +10 for longest route
        assertEquals( 12 + 10, player.getScore());
    }

    @Test
    void testCalculateFinalScoreWithLongestRoute(){
        PlayerModel playerWithLongestRoute = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithLongestRoute.defineStartingStationsNumber(3);
        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 6, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 6, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeBerlinToTokyo = new Route(new City("Berlin"), new City("Tokyo"), 6, RouteType.TRAIN, Color.BLACK, 5);

        PlayerModel otherPlayer = new PlayerModel(PlayerIdentification.GREEN, null);
        otherPlayer.defineStartingStationsNumber(3);
        Route routeEdinburghToTokyo = new Route(new City("Edinburgh"), new City("Tokyo"), 3, RouteType.TRAIN, Color.BLACK, 5);
        routeEdinburghToTokyo.setClaimerPlayer(PlayerIdentification.GREEN);
        Route routeTokyoToParis = new Route(new City("Tokyo"), new City("Paris"), 4, RouteType.TRAIN, Color.BLACK, 5);
        routeTokyoToParis.setClaimerPlayer(PlayerIdentification.GREEN);
        Route routeParisToBerlin = new Route(new City("Paris"), new City("Berlin"), 4, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToBerlin.setClaimerPlayer(PlayerIdentification.GREEN);
        Route routeBerlinToLondon = new Route(new City("Berlin"), new City("London"), 4, RouteType.TRAIN, Color.BLACK, 5);
        routeBerlinToLondon.setClaimerPlayer(PlayerIdentification.GREEN);

        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid, routeBerlinToTokyo));
        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.GREEN)).thenReturn(List.of(routeEdinburghToTokyo, routeTokyoToParis, routeParisToBerlin, routeBerlinToLondon));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithLongestRoute, otherPlayer));

        endGameScoreController.setFinalScores();
        assertEquals(15 + 15 + 15 + 10 + 12, playerWithLongestRoute.getScore());
        assertEquals(4 + 21 + 12, otherPlayer.getScore());
    }

    /**
     * Test the calculation of the final score with a loop in the longest route
     * Expected behavior : the loop is taken into account in the calculation of the longest route
     */
    /*@Test
    void testCalculateFinalScoreWithLongestRouteLooping() {
        PlayerModel playerWithLoopingRoute = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithLoopingRoute.defineStartingStationsNumber(3);
        Route routeMunchenToWien = new Route(new City("Munchen"), new City("Wien"), 3, RouteType.TRAIN, Color.BLACK, 5);
        routeMunchenToWien.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeWienToBerlin = new Route(new City("Wien"), new City("Berlin"), 3, RouteType.TRAIN, Color.BLACK, 5);
        routeWienToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeBerlinToMunchen = new Route(new City("Berlin"), new City("Munchen"), 2, RouteType.TRAIN, Color.BLACK, 5);
        routeBerlinToMunchen.setClaimerPlayer(PlayerIdentification.BLACK);
        Route routeMunchenToZurich = new Route(new City("Munchen"), new City("Zurich"), 4, RouteType.TRAIN, Color.BLACK, 5);
        routeMunchenToZurich.setClaimerPlayer(PlayerIdentification.BLACK);

        PlayerModel otherPlayer = new PlayerModel(PlayerIdentification.GREEN, null);
        otherPlayer.defineStartingStationsNumber(3);
        Route routeDanzicToRiga = new Route(new City("Danzic"), new City("Riga"), 6, RouteType.TRAIN, Color.BLACK, 5);
        routeDanzicToRiga.setClaimerPlayer(PlayerIdentification.GREEN);
        Route routeRigaToPetrograd = new Route(new City("Riga"), new City("Petrograd"), 4, RouteType.TRAIN, Color.BLACK, 5);
        routeRigaToPetrograd.setClaimerPlayer(PlayerIdentification.GREEN);

        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMunchenToWien, routeWienToBerlin, routeBerlinToMunchen, routeMunchenToZurich));
        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.GREEN)).thenReturn(List.of(routeDanzicToRiga, routeRigaToPetrograd));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithLoopingRoute, otherPlayer));

        endGameScoreController.setFinalScores();
        assertEquals(4 + 4 + 2 + 7 + 10 + 12, playerWithLoopingRoute.getScore());
        assertEquals(15 + 7 + 12, otherPlayer.getScore());
    }*/
}
