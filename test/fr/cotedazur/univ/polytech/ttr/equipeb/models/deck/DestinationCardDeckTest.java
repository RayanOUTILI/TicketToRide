package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DestinationCardDeckTest {

    private DestinationCardDeck<DestinationCard> destinationCardDeck;

    @BeforeEach
    void setup() {
        List<DestinationCard> cards = new ArrayList<>(List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class)));
        destinationCardDeck = new DestinationCardDeck<>(cards);
    }

    @Test
    void drawCard() {
        List<DestinationCard> cards = destinationCardDeck.drawCard(2);
        assertEquals(2, cards.size());
    }

    @Test
    void addCardsAtBottom() {
        List<DestinationCard> cards = new ArrayList<>(List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class)));
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
        List<DestinationCard> cards = destinationCardDeck.drawCard(3);
        destinationCardDeck.shuffle();
        List<DestinationCard> shuffledCards = destinationCardDeck.drawCard(3);
        assertNotEquals(cards, shuffledCards);
    }
}