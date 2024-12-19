package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerModelTest {

    private PlayerModel playerModel;

    @BeforeEach
    void setUp() {
        playerModel = new PlayerModel(PlayerIdentification.BLUE);
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
}