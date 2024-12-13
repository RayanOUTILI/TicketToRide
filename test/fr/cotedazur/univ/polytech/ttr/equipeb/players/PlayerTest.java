package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.NotEnoughCardsException;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.RouteAlreadyClaimedException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class PlayerTest {

    private City city1, city2;
    private Route route;
    private Player player1, player2;

    @BeforeEach
    void setUp() {
        city1 = new City("City1");
        city2 = new City("City2");
        route = new Route(city1, city2, 3);
        player1 = new Player();
        player2 = new Player();
    }

    @Test
    void testClaimRouteAddsPoints() throws NotEnoughCardsException, RouteAlreadyClaimedException {
        City city1 = new City("City1");
        City city2 = new City("City2");
        Route route = new Route(city1, city2, 3);
        Player player = new Player();

        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());

        player.claimRoute(route);

        assertEquals(4, player.getScore(), "Player should earn 4 points for a length-3 route");
    }

    @Test
    void testScoreMultipleRoutes() throws NotEnoughCardsException, RouteAlreadyClaimedException {
        City city1 = new City("City1");
        City city2 = new City("City2");
        City city3 = new City("City3");

        Route route1 = new Route(city1, city2, 2); // Length 2 -> 2 points
        Route route2 = new Route(city2, city3, 4); // Length 4 -> 7 points

        Player player = new Player();

        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.claimRoute(route1);

        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.claimRoute(route2);

        assertEquals(9, player.getScore(), "Player should earn 9 points in total (2 + 7)");
    }

    @Test
    void testClaimRouteNotEnoughCards() {
        City city1 = new City("City1");
        City city2 = new City("City2");
        Route route = new Route(city1, city2, 3);

        Player player = new Player();

        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());

        assertThrows(NotEnoughCardsException.class, () -> player.claimRoute(route),
                "Player should not be able to claim a route without enough wagon cards");
        assertEquals(0, player.getScore(), "Player's score should remain 0 if route claim fails");
    }

    @Test
    void testClaimFreeRoute() throws RouteAlreadyClaimedException {
        player1.pickCard(new WagonCard());
        player1.pickCard(new WagonCard());
        player1.pickCard(new WagonCard());
        player1.claimRoute(route);

        assertTrue(player1.getOwnedRoutes().contains(route), "The route should be owned by player1");
        assertEquals(0, player1.getHand().size(), "Player1 should have no cards left after claiming the route");
        assertEquals(3, route.getPlacedWagons(),
                "The number of placed wagons on the route should match the route length");
    }

    @Test
    void testClaimTakenRoute() throws RouteAlreadyClaimedException {
        player1.pickCard(new WagonCard());
        player1.pickCard(new WagonCard());
        player1.pickCard(new WagonCard());
        player1.claimRoute(route);

        player2.pickCard(new WagonCard());

        assertThrows(RouteAlreadyClaimedException.class, () -> player2.claimRoute(route),
                "Player 2 should not be able to claim the route that Player 1 has already claimed");
    }

    @Test
    void testClaimRouteWithoutWagonCard() {
        assertThrows(NotEnoughCardsException.class, () -> player1.claimRoute(route),
                "Player should not be able to claim the route without enough wagon cards");
    }

    @Test
    public void testPlayerHasNoCardsInitially() {
        Player player = new Player();
        assertTrue(player.getHand().isEmpty(), "Player's hand must be empty.");
    }

    @Test
    public void testPlayerPicksThreeCards() {
        Player player = new Player();
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        player.pickCard(new WagonCard());
        assertEquals(3, player.getHand().size(), "Player's hand must contains 3 cards.");
    }
}
