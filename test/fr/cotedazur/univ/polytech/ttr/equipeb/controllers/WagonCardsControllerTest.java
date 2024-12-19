package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WagonCardsControllerTest {

    private WagonCardsController wagonCardsController;
    private IWagonCardsControllerGameModel gameModel;
    private Player player;
    private WagonCard wagonCard;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IWagonCardsControllerGameModel.class);
        wagonCardsController = new WagonCardsController(gameModel);
        player = mock(Player.class);
        wagonCard = mock(WagonCard.class);
    }

    @org.junit.jupiter.api.Test
    void testDoActionWithWagonCardDeckEmpty() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(true);
        assertFalse(wagonCardsController.doAction(player));
        verify(gameModel).isWagonCardDeckEmpty();
    }

    @org.junit.jupiter.api.Test
    void testDoActionWithNonCardDeckEmpty() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawCardFromWagonCardDeck()).thenReturn(wagonCard);
        assertTrue(wagonCardsController.doAction(player));
        verify(gameModel).isWagonCardDeckEmpty();
        verify(gameModel).drawCardFromWagonCardDeck();
        verify(player).receivedWagonCard(wagonCard);
    }

}