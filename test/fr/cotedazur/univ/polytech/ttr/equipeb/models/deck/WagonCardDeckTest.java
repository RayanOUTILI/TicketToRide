package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WagonCardDeckTest {

    private WagonCardDeck wagonCardDeck;

    @BeforeEach
    void setup() {
        List<WagonCard> cards = new ArrayList<>(List.of(mock(WagonCard.class)));
        wagonCardDeck = new WagonCardDeck(cards);
    }

    @Test
    void drawCard() {
        wagonCardDeck.drawCard();
        assertThrows(IllegalStateException.class, wagonCardDeck::drawCard);
    }

    @Test
    void isEmpty() {
        wagonCardDeck.drawCard();
        assertTrue(wagonCardDeck.isEmpty());
    }

    @Test
    void shuffle() {
        assertTrue(wagonCardDeck.shuffle());

    }

    @Test
    void addCardToDiscardPile() {
        WagonCard card = mock(WagonCard.class);
        assertTrue(wagonCardDeck.addCardToDiscardPile(List.of(card)));

    }

    @Test
    void fillDeck() {
        assertFalse(wagonCardDeck.fillDeck());
        WagonCard card = wagonCardDeck.drawCard();
        assertTrue(wagonCardDeck.addCardToDiscardPile(List.of(card)));
        assertTrue(wagonCardDeck.fillDeck());
        assertEquals(card, wagonCardDeck.drawCard());
    }
}
