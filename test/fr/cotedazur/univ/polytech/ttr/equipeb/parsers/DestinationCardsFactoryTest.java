package fr.cotedazur.univ.polytech.ttr.equipeb.parsers;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.DestinationCardsFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.DestinationCardsParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class DestinationCardsFactoryTest {

    private DestinationCardsFactory destinationCardsFactory;
    private DestinationCardsParser parserMock;

    /**
     * Initializes the factory and mock parser before each test.
     */
    @BeforeEach
    void setUp() {
        parserMock = mock(DestinationCardsParser.class);
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
            assertTrue(cards.get(0) instanceof ShortDestinationCard);
            assertTrue(cards.get(41) instanceof LongDestinationCard);

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
            assertTrue(cards.get(0) instanceof ShortDestinationCard);

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
            assertTrue(cards.get(0) instanceof LongDestinationCard);

        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test to ensure that an exception is thrown when the JSON file is not found.
     */
    @Test
    void testFileNotFound() {
        //TODO
    }
}
