package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.RouteAlreadyClaimedException;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.NotEnoughCardsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
