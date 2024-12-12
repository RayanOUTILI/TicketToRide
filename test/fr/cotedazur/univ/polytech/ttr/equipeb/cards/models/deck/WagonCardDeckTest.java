package fr.cotedazur.univ.polytech.ttr.equipeb.cards.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.cards.models.cards.WagonCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WagonCardDeckTest {
    WagonCardDeck wagonCardDeckInitEmpty;
    WagonCardDeck wagonCardDeckInitFilled;

    WagonCard card1;
    WagonCard card2;
    WagonCard card3;

    @BeforeEach
    void setUp() {
        wagonCardDeckInitEmpty = new WagonCardDeck();
        card1 = new WagonCard();
        card2 = new WagonCard();
        card3 = new WagonCard();
        List<WagonCard> cards = List.of(card1, card2, card3);
        wagonCardDeckInitFilled = new WagonCardDeck(cards);
    }

    @Test
    void testInit() {
        assertEquals(0, wagonCardDeckInitEmpty.size());
        assertEquals(3, wagonCardDeckInitFilled.size());
    }

    @Test
    void testPickCard() {
        WagonCard card = wagonCardDeckInitFilled.drawCard();
        assertEquals(2, wagonCardDeckInitFilled.size());
        assertEquals(card, card1);
        assertEquals(card2, wagonCardDeckInitFilled.getWagonCard(0));
        assertEquals(card3, wagonCardDeckInitFilled.getWagonCard(1));
    }

    @Test
    void testPickCardEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> wagonCardDeckInitEmpty.drawCard());
    }
}