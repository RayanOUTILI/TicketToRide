package fr.cotedazur.univ.polytech.ttr.equipeb.models.deck;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WagonCardDeckTest {

    private WagonCardDeck wagonCardDeck;
    private List<WagonCard> shownCards;
    private Field shownCardsField;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        List<WagonCard> cards = new ArrayList<>(List.of(mock(WagonCard.class)));
        wagonCardDeck = new WagonCardDeck(cards);

        shownCardsField = WagonCardDeck.class.getDeclaredField("shownCards");
        shownCardsField.setAccessible(true);
        shownCards = mock(List.class);
        shownCardsField.set(wagonCardDeck, shownCards);
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

    @Test
    void addCardToShownCards() {
        WagonCard card = mock(WagonCard.class);

        when(shownCards.size()).thenReturn(5);
        assertFalse(wagonCardDeck.addCardToShownCards(card));

        when(shownCards.size()).thenReturn(1);
        when(shownCards.add(card)).thenReturn(true);
        assertTrue(wagonCardDeck.addCardToShownCards(card));

        when(shownCards.size()).thenReturn(3);
        when(shownCards.add(card)).thenReturn(false);
        assertFalse(wagonCardDeck.addCardToShownCards(card));
    }

    @Test
    void removeCardFromShownCards() {
        WagonCard card = mock(WagonCard.class);
        when(shownCards.remove(card)).thenReturn(true);
        assertTrue(wagonCardDeck.removeCardFromShownCards(card));

        when(shownCards.remove(card)).thenReturn(false);
        assertFalse(wagonCardDeck.removeCardFromShownCards(card));
    }

    @Test
    void shownCards() throws IllegalAccessException {
        this.shownCards = new ArrayList<>();
        this.shownCards.add(mock(WagonCard.class));
        this.shownCardsField.set(wagonCardDeck, shownCards);

        List<WagonCard> shownCardsReturnedList = wagonCardDeck.shownCards();
        assertEquals(shownCards, shownCardsReturnedList);

        assertNotSame(shownCards, shownCardsReturnedList);
    }

    @Test
    void replaceShownCards() throws IllegalAccessException {
        this.shownCards = new ArrayList<>();
        this.shownCards.add(mock(WagonCard.class));
        this.shownCardsField.set(wagonCardDeck, shownCards);

        List<WagonCard> cards = new ArrayList<>(List.of(mock(WagonCard.class)));
        boolean result = wagonCardDeck.replaceShownCards(cards);
        assertTrue(result);
        assertEquals(shownCards, cards);

        assertNotSame(shownCards, cards);
    }
}
