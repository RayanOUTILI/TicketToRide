package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

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

//    @org.junit.jupiter.api.BeforeEach
//    void setUp() {
//        gameModel = mock(IScoreControllerGameModel.class);
//        scoreController = new ScoreController(gameModel);
//        player = new PlayerModel(PlayerIdentification.BLACK, null);
//
//        routeLength1 = mock(RouteReadOnly.class);
//            when(routeLength1.getLength()).thenReturn(1);
//            when(routeLength1.isClaimed()).thenReturn(true);
//        routeLength2 = mock(RouteReadOnly.class);
//            when(routeLength2.getLength()).thenReturn(2);
//            when(routeLength2.isClaimed()).thenReturn(true);
//        routeLength3 = mock(RouteReadOnly.class);
//            when(routeLength3.getLength()).thenReturn(3);
//            when(routeLength3.isClaimed()).thenReturn(true);
//        routeLength4 = mock(RouteReadOnly.class);
//            when(routeLength4.getLength()).thenReturn(4);
//            when(routeLength4.isClaimed()).thenReturn(true);
//        routeLength6 = mock(RouteReadOnly.class);
//            when(routeLength6.getLength()).thenReturn(6);
//            when(routeLength6.isClaimed()).thenReturn(true);
//        routeLength8 = mock(RouteReadOnly.class);
//            when(routeLength8.getLength()).thenReturn(8);
//            when(routeLength8.isClaimed()).thenReturn(true);
//        getRouteLengthOther = mock(RouteReadOnly.class);
//            when(getRouteLengthOther.getLength()).thenReturn(5);
//            when(getRouteLengthOther.isClaimed()).thenReturn(true);
//    }
//
//    @org.junit.jupiter.api.Test
//    void testUpdateScoreWithMultipleRoutes() {
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1, routeLength2, routeLength3, routeLength4, routeLength6, routeLength8, getRouteLengthOther));
//        scoreController.updateScore(player);
//        assertEquals((1 + 2 + 4 + 7 + 15 + 21 + 0), player.getScore());
//    }
//
//    @org.junit.jupiter.api.Test
//    void testUpdateScoreWithNoRoutes() {
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of());
//        scoreController.updateScore(player);
//        assertEquals(0, player.getScore());
//    }
//
//    @org.junit.jupiter.api.Test
//    void testUpdateScoreWithOneRoute() {
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength1));
//        scoreController.updateScore(player);
//        assertEquals(1, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength2));
//        scoreController.updateScore(player);
//        assertEquals(2, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength3));
//        scoreController.updateScore(player);
//        assertEquals(4, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength4));
//        scoreController.updateScore(player);
//        assertEquals(7, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength6));
//        scoreController.updateScore(player);
//        assertEquals(15, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(routeLength8));
//        scoreController.updateScore(player);
//        assertEquals(21, player.getScore());
//
//        when(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())).thenReturn(List.of(getRouteLengthOther));
//        scoreController.updateScore(player);
//        assertEquals(0, player.getScore());
//    }
//
//    @Test
//    void testCalculateDestinationsScores() {
//        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);
//
//        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
//        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));
//
//        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
//        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);
//
//        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
//        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
//
//
//        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
//        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
//        scoreController.calculateFinalScores();
//        assertEquals(10, playerWithDestinations.getScore());
//    }
//
//    @Test
//    void testCalculateDestinationsScoresWithAnIncompleteObjective() {
//        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Berlin"), new City("Paris"), 10);
//        ShortDestinationCard parisToDakarDestination = new ShortDestinationCard(new City("Paris"), new City("Dakar"), 5);
//
//        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
//        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination, parisToDakarDestination));
//
//        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
//        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);
//
//        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
//        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
//
//
//        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid));
//        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
//        scoreController.calculateFinalScores();
//        assertEquals(5, playerWithDestinations.getScore());
//    }
//
//    @Test
//    void testCyclicDestinationsScores() {
//        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);
//
//        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
//        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));
//
//        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 0, RouteType.TRAIN, Color.BLACK , 5);
//        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);
//
//        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 0, RouteType.TRAIN, Color.BLACK, 5);
//        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);
//
//        Route routeBerlinToParis = new Route(new City("Berlin"), new City("Paris"), 0, RouteType.TRAIN, Color.BLACK, 5);
//        routeBerlinToParis.setClaimerPlayer(PlayerIdentification.BLACK);
//
//
//        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid, routeBerlinToParis));
//        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
//        scoreController.calculateFinalScores();
//        assertEquals(10, playerWithDestinations.getScore());
//    }

    /*@Test
    //TODO: Test this in graph utils Test
    void testCalculateDestinationWithALoopCity() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 5, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToMarseille = new Route(new City("Madrid"), new City("Marseille"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToMarseille.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMarseilleToNice = new Route(new City("Marseille"), new City("Nice"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMarseilleToNice.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeNiceToMadrid = new Route(new City("Nice"), new City("Madrid"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeNiceToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);

        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid, routeMadridToMarseille, routeMarseilleToNice, routeNiceToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        Set<CityPair> allClaimedCityPair = scoreController.getAllCityPairs(playerWithDestinations);
        System.out.println(allClaimedCityPair.size());
        assertEquals(13, allClaimedCityPair.size());

        System.out.println("All claimed city pairs:");
        for (CityPair pair : allClaimedCityPair) {
            System.out.println(pair);
        }

        // Check if the length of the route between Paris and Berlin is the highest of all possible routes
        // beetwen Paris and Berlin
        // it should be:
        // Paris -> Madrid -> Marseille -> Nice -> Madrid -> Berlin
        //        5    +    5     +     5   +   5     +    5        = 25

        Optional<CityPair> parisToBerlin = allClaimedCityPair.stream()
                .filter(pair -> pair.equals(new CityPair(new City("Paris"), new City("Berlin"))))
                .findFirst();

        assertTrue(parisToBerlin.isPresent());

        assertEquals(10, parisToBerlin.get().getMinLength());
        assertEquals(25, parisToBerlin.get().getMaxLength());
    }*/
}
