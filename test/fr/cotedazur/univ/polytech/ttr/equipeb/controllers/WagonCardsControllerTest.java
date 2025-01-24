package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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

    @Test
    void testInitErrorShuffle() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(false);
        assertFalse(wagonCardsController.init(player));
    }

    @Test
    void testInitErrorDraw() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(null);
        assertFalse(wagonCardsController.init(player));
    }

    @Test
    void testInitErrorDrawSize() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(List.of(wagonCard, wagonCard, wagonCard));
        assertFalse(wagonCardsController.init(player));
    }

    @Test
    void testInit() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(List.of(wagonCard, wagonCard, wagonCard, wagonCard));
        assertTrue(wagonCardsController.init(player));
        verify(gameModel).isWagonCardDeckEmpty();
        verify(gameModel).shuffleWagonCardDeck();
        verify(gameModel).drawCardsFromWagonCardDeck(4);
        verify(player).receivedWagonCards(List.of(wagonCard, wagonCard, wagonCard, wagonCard));
    }

    @org.junit.jupiter.api.Test
    void testDoActionWithWagonCardDeckEmpty() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(true);
        Optional<ReasonActionRefused> actionRefused = wagonCardsController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.WAGON_CARDS_DECK_EMPTY, actionRefused.get());
        verify(gameModel).isWagonCardDeckEmpty();
    }

    @org.junit.jupiter.api.Test
    void testDoActionWithNonCardDeckEmpty() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawCardFromWagonCardDeck()).thenReturn(wagonCard);
        Optional<ReasonActionRefused> actionRefused = wagonCardsController.doAction(player);
        assertTrue(actionRefused.isEmpty());
        verify(gameModel).isWagonCardDeckEmpty();
        verify(gameModel).drawCardFromWagonCardDeck();
        verify(player).receivedWagonCard(wagonCard);
    }

}