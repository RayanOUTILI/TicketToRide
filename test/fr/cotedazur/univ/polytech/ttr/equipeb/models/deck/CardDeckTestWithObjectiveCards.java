package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.Card;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardDeckTestWithObjectiveCards {
    CardDeck cardDeckInitEmpty;
    CardDeck cardDeckInitFilled;

    ObjectiveCard card1;
    ObjectiveCard card2;
    ObjectiveCard card3;

    @BeforeEach
    void setUp() {
        cardDeckInitEmpty = new CardDeck();
        card1 = new ObjectiveCard();
        card2 = new ObjectiveCard();
        card3 = new ObjectiveCard();
        List<Card> cards = List.of(card1, card2, card3);
        cardDeckInitFilled = new CardDeck(cards);
    }

    @Test
    void testInit() {
        assertEquals(0, cardDeckInitEmpty.size());
        assertEquals(3, cardDeckInitFilled.size());
    }

    @Test
    void testPickCard() {
        Card card = cardDeckInitFilled.drawCard();
        assertEquals(2, cardDeckInitFilled.size());
        assertEquals(card, card1);
        assertEquals(card2, cardDeckInitFilled.getCard(0));
        assertEquals(card3, cardDeckInitFilled.getCard(1));
    }

    @Test
    void testPickCardEmptyDeck() {
        assertThrows(IllegalStateException.class, () -> cardDeckInitEmpty.drawCard());
    }

    @Test
    void testRegenerateDeck() {
        ObjectiveCard card4 = new ObjectiveCard();
        List<Card> cards = List.of(card4);
        cardDeckInitEmpty.regenerateDeck(cards);
        assertEquals(1, cardDeckInitEmpty.size());
        assertEquals(card4, cardDeckInitEmpty.getCard(0));
    }
}