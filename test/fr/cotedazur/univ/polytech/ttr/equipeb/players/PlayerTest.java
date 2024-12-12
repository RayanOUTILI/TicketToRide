package fr.cotedazur.univ.polytech.ttr.equipeb.players;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testPlayerHasNoCardsInitially() {
        Player player = new Player();
        assertTrue(player.getHand().isEmpty(), "Player's hand must be empty.");
    }

    @Test
    public void testPlayerPicksThreeCards() {
        Player player = new Player();

        player.pickCard();
        player.pickCard();
        player.pickCard();

        assertEquals(3, player.getHand().size(), "Player's hand must contains 3 cards.");
    }
}
