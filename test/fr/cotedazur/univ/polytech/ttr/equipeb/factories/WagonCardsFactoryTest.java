package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WagonCardsFactoryTest {

    @Test
    void getWagonCardsReturnsCorrectNumberOfCards() {
        WagonCardsFactory factory = new WagonCardsFactory();
        List<WagonCard> wagonCards = factory.getWagonCards();

        assertEquals(110, wagonCards.size());
    }

    @Test
    void getWagonCardsReturnsCorrectNumberOfEachColor() {
        WagonCardsFactory factory = new WagonCardsFactory();
        List<WagonCard> wagonCards = factory.getWagonCards();

        long pinkCount = wagonCards.stream().filter(card -> card.getColor() == Color.PINK).count();
        long whiteCount = wagonCards.stream().filter(card -> card.getColor() == Color.WHITE).count();
        long blueCount = wagonCards.stream().filter(card -> card.getColor() == Color.BLUE).count();
        long yellowCount = wagonCards.stream().filter(card -> card.getColor() == Color.YELLOW).count();
        long orangeCount = wagonCards.stream().filter(card -> card.getColor() == Color.ORANGE).count();
        long blackCount = wagonCards.stream().filter(card -> card.getColor() == Color.BLACK).count();
        long redCount = wagonCards.stream().filter(card -> card.getColor() == Color.RED).count();
        long greenCount = wagonCards.stream().filter(card -> card.getColor() == Color.GREEN).count();
        long anyCount = wagonCards.stream().filter(card -> card.getColor() == Color.ANY).count();

        assertEquals(12, pinkCount);
        assertEquals(12, whiteCount);
        assertEquals(12, blueCount);
        assertEquals(12, yellowCount);
        assertEquals(12, orangeCount);
        assertEquals(12, blackCount);
        assertEquals(12, redCount);
        assertEquals(12, greenCount);
        assertEquals(14, anyCount);
    }

    @Test
    void getWagonCardsHandlesEmptyList() {
        WagonCardsFactory factory = new WagonCardsFactory();
        List<WagonCard> wagonCards = factory.getWagonCards();

        assertNotNull(wagonCards);
        assertFalse(wagonCards.isEmpty());
    }
}