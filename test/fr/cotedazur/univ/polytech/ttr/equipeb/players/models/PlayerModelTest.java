package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerModelTest {

    private PlayerModel playerModel;

    private List<WagonCard> getWagonCards(List<Color> colors) {
        ArrayList<WagonCard> wagonCards = new ArrayList<>();
        for (Color color : colors) {
            WagonCard wagonCard = mock(WagonCard.class);
            when(wagonCard.getColor()).thenReturn(color);
            wagonCards.add(wagonCard);
        }

        return wagonCards;
    }

    @BeforeEach
    void setUp() {
        playerModel = new PlayerModel(PlayerIdentification.BLUE, null);
    }

    @org.junit.jupiter.api.Test
    void getIdentification() {
        assertEquals(PlayerIdentification.BLUE, playerModel.getIdentification());
    }

    @org.junit.jupiter.api.Test
    void receivedWagonCard() {
        WagonCard wagonCard = mock(WagonCard.class);
        playerModel.receivedWagonCard(wagonCard);
        assertEquals(1, playerModel.getNumberOfWagonCards());
    }

    @org.junit.jupiter.api.Test
    void removeWagonCards() {
        WagonCard wagonCard = mock(WagonCard.class);
        playerModel.receivedWagonCard(wagonCard);
        assertEquals(1, playerModel.getNumberOfWagonCards());
        playerModel.removeWagonCards(List.of(wagonCard));
        assertEquals(0, playerModel.getNumberOfWagonCards());
    }

    @org.junit.jupiter.api.Test
    void testGetWagonCards() {
        WagonCard wagonCard = mock(WagonCard.class);
        assertEquals(0, playerModel.getWagonCards(0).size());
        playerModel.receivedWagonCard(wagonCard);
        assertEquals(1, playerModel.getNumberOfWagonCards());
    }

    @Test
    void testGetWagonCardsIncludingAnyColor() {
        WagonCard wagonCard = mock(WagonCard.class);
        when(wagonCard.getColor()).thenReturn(Color.BLUE);
        playerModel.receivedWagonCards(List.of(wagonCard));
        assertEquals(1, playerModel.getWagonCardsIncludingAnyColor(Color.BLUE, 1, 0).size());
    }

    @Test
    void testGetWagonCardsIncludingAnyColorAny() {
        List<WagonCard> wagonCards = getWagonCards(List.of(Color.BLUE, Color.ANY, Color.ANY, Color.ANY, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW));
        playerModel.receivedWagonCards(wagonCards);
        List<WagonCard> cards;
        cards = playerModel.getWagonCardsIncludingAnyColor(Color.BLUE, 6, 2);
        assertEquals(0, cards.size());

        cards = playerModel.getWagonCardsIncludingAnyColor(Color.ANY, 4, 1);
        assertEquals(5, cards.size());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.BLUE).count());
        assertEquals(3, cards.stream().filter(card -> card.getColor() == Color.ANY).count());

        cards = playerModel.getWagonCardsIncludingAnyColor(Color.BLUE, 4, 0);
        assertEquals(4, cards.size());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.BLUE).count());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.ANY).count());

        cards = playerModel.getWagonCardsIncludingAnyColor(Color.BLUE, 4, 2);
        assertEquals(0, cards.size());
    }

    @Test
    void testGetWagonCardsIncludingAnyColorOnlyByNumber() {
        List<WagonCard> wagonCards = getWagonCards(List.of(Color.BLUE, Color.ANY, Color.ANY, Color.ANY, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW));
        playerModel.receivedWagonCards(wagonCards);

        List<WagonCard> cards;

        cards = playerModel.getWagonCardsIncludingAnyColor(2);
        assertEquals(2, cards.size());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.BLUE).count());

        cards = playerModel.getWagonCardsIncludingAnyColor(4);
        assertEquals(4, cards.size());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.BLUE).count());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.ANY).count());


        cards = playerModel.getWagonCardsIncludingAnyColor(6);
        assertEquals(5, cards.size());
        assertEquals(2, cards.stream().filter(card -> card.getColor() == Color.BLUE).count());
        assertEquals(3, cards.stream().filter(card -> card.getColor() == Color.ANY).count());
    }
}