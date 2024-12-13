package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.Card;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTestWithWagonCards {
    CardDeck wagonCardDeckInitEmpty;
    CardDeck wagonCardDeckInitFilled;

    WagonCard card1;
    WagonCard card2;
    WagonCard card3;

    @BeforeEach
    void setUp() {
        wagonCardDeckInitEmpty = new CardDeck();
        card1 = new WagonCard();
        card2 = new WagonCard();
        card3 = new WagonCard();
        List<Card> cards = List.of(card1, card2, card3);
        wagonCardDeckInitFilled = new CardDeck(cards);
    }

    @Test
    void testInit() {
        assertEquals(0, wagonCardDeckInitEmpty.size());
        assertEquals(3, wagonCardDeckInitFilled.size());
    }

    @Test
    void testPickCard() {
        Card card = wagonCardDeckInitFilled.drawCard();
        assertEquals(2, wagonCardDeckInitFilled.size());
        assertEquals(card, card1);
        assertEquals(card2, wagonCardDeckInitFilled.getCard(0));
        assertEquals(card3, wagonCardDeckInitFilled.getCard(1));
    }

    @Test
    void testPickCardEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> wagonCardDeckInitEmpty.drawCard());
    }

    @Test
    void testRegenerateDeck() {
        WagonCard card4 = new WagonCard();
        List<Card> cards = List.of(card4);
        wagonCardDeckInitEmpty.regenerateDeck(cards);
        assertEquals(1, wagonCardDeckInitEmpty.size());
        assertEquals(card4, wagonCardDeckInitEmpty.getCard(0));
    }
}