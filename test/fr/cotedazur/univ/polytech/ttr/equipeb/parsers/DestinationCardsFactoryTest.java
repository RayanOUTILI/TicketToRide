package fr.cotedazur.univ.polytech.ttr.equipeb.parsers;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DestinationCardsFactoryTest {
    private DestinationCardsFactory destinationCardsFactory;

    /**
     * Initializes the factory and mock parser before each test.
     */
    @BeforeEach
    void setUp() {
        destinationCardsFactory = new DestinationCardsFactory();
    }

    /**
     * Test to verify retrieving all destination cards (short and long).
     */
    @Test
    void testGetAllDestinationCards() {
        try {
            List<DestinationCard> cards = destinationCardsFactory.getAllDestinationCards();
            assertNotNull(cards);
            assertEquals(46, cards.size());
            assertInstanceOf(ShortDestinationCard.class, cards.get(0));
            assertInstanceOf(LongDestinationCard.class, cards.get(41));

        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test to verify retrieving only short destination cards.
     */
    @Test
    void testGetShortDestinationCards() {
        try {
            List<ShortDestinationCard> cards = destinationCardsFactory.getShortDestinationCards();
            assertNotNull(cards);
            assertEquals(40, cards.size());
            assertInstanceOf(ShortDestinationCard.class, cards.get(0));

        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test to verify retrieving only long destination cards.
     */
    @Test
    void testGetLongDestinationCards() {
        try {
            List<LongDestinationCard> cards = destinationCardsFactory.getLongDestinationCards();
            assertNotNull(cards);
            assertEquals(6, cards.size());
            assertInstanceOf(LongDestinationCard.class, cards.getFirst());

        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }
}
