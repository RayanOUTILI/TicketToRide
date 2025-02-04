package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DestinationCardDeckTest {

    private DestinationCardDeck<ShortDestinationCard> destinationCardDeck;

    @BeforeEach
    void setup() {
        List<ShortDestinationCard> cards = new ArrayList<>(List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class)));
        destinationCardDeck = new DestinationCardDeck<>(cards);
    }

    @Test
    void drawCard() {
        List<ShortDestinationCard> cards = destinationCardDeck.drawCard(2);
        assertEquals(2, cards.size());
    }

    @Test
    void addCardsAtBottom() {
        List<ShortDestinationCard> cards = new ArrayList<>(List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class)));
        destinationCardDeck.addCardsAtBottom(cards);
        assertEquals(6, destinationCardDeck.drawCard(6).size());
    }

    @Test
    void isEmpty() {
        assertFalse(destinationCardDeck.isEmpty());
        destinationCardDeck.drawCard(3);
        assertTrue(destinationCardDeck.isEmpty());
    }

    @Test
    void shuffle() {
        List<ShortDestinationCard> cards = destinationCardDeck.drawCard(3);
        destinationCardDeck.shuffle();
        List<ShortDestinationCard> shuffledCards = destinationCardDeck.drawCard(3);
        assertNotEquals(cards, shuffledCards);
    }
}