package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ActionDrawWagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IWagonCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void initGame() {
        WagonCard locomotive = mock(WagonCard.class);
        when(locomotive.getColor()).thenReturn(Color.ANY);
        List<WagonCard> shownCards = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));
        shownCards.forEach(card -> when(card.getColor()).thenReturn(Color.BLUE));
        shownCards.add(locomotive);

        when(gameModel.placeShownWagonCards(anyList())).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(5)).thenReturn(shownCards);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);

        assertTrue(wagonCardsController.initGame());
    }

    @Test
    void testInitPlayerErrorShuffle() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(false);
        assertFalse(wagonCardsController.initPlayer(player));
    }

    @Test
    void testInitPlayerErrorDraw() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(null);
        assertFalse(wagonCardsController.initPlayer(player));
    }

    @Test
    void testInitPlayerErrorDrawSize() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(List.of(wagonCard, wagonCard, wagonCard));
        assertFalse(wagonCardsController.initPlayer(player));
    }

    @Test
    void testInitPlayer() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleWagonCardDeck()).thenReturn(true);
        when(gameModel.drawCardsFromWagonCardDeck(4)).thenReturn(List.of(wagonCard, wagonCard, wagonCard, wagonCard));
        assertTrue(wagonCardsController.initPlayer(player));
        verify(gameModel).isWagonCardDeckEmpty();
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
        when(player.askDrawWagonCard(anyList())).thenReturn(Optional.of(ActionDrawWagonCard.DRAW_FROM_DECK), Optional.empty());
        when(gameModel.drawCardFromWagonCardDeck()).thenReturn(wagonCard);
        Optional<ReasonActionRefused> actionRefused = wagonCardsController.doAction(player);
        assertTrue(actionRefused.isEmpty());
        verify(gameModel, times(1)).drawCardFromWagonCardDeck();
        verify(player, times(1)).receivedWagonCard(wagonCard);
    }

    @Test
    void actionDrawFromShownCardsLocomotive() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);
        when(player.askDrawWagonCard(any())).thenReturn(Optional.of(ActionDrawWagonCard.DRAW_FROM_SHOWN_CARDS));
        WagonCard locomotive = mock(WagonCard.class);
        when(locomotive.getColor()).thenReturn(Color.ANY);
        List<WagonCard> shownCards = new ArrayList<>(List.of(mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class), mock(WagonCard.class)));
        shownCards.forEach(card -> when(card.getColor()).thenReturn(Color.BLUE));
        shownCards.add(locomotive);
        when(gameModel.getListOfShownWagonCards()).thenReturn(shownCards);
        when(gameModel.removeCardFromShownCards(locomotive)).thenReturn(true);

        when(player.askWagonCardFromShownCards()).thenReturn(locomotive);

        Optional<ReasonActionRefused> actionRefused = wagonCardsController.doAction(player);
        assertTrue(actionRefused.isEmpty());

        verify(player, times(1)).askDrawWagonCard(any());

        verify(gameModel, times(1)).removeCardFromShownCards(locomotive);

        verify(player, times(1)).askWagonCardFromShownCards();
        verify(player, times(1)).receivedWagonCard(locomotive);
    }

    @Test
    void testChooseNonePossibleAction() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false, true);
        when(player.askDrawWagonCard(any())).thenReturn(Optional.of(ActionDrawWagonCard.DRAW_FROM_DECK));
        Optional<ReasonActionRefused> actionRefused = wagonCardsController.doAction(player);
        assertTrue(actionRefused.isPresent());
        assertEquals(ReasonActionRefused.WAGON_CARDS_ACTION_INVALID, actionRefused.get());
    }

    @Test
    void testResetPlayer() {
        when(player.getWagonCardsHand()).thenReturn(List.of(wagonCard, wagonCard, wagonCard));
        when(player.removeWagonCards(anyList())).thenReturn(List.of(wagonCard, wagonCard, wagonCard));
        when(gameModel.discardWagonCards(anyList())).thenReturn(true);
        assertTrue(wagonCardsController.resetPlayer(player));
        verify(player).getWagonCardsHand();
        verify(player).removeWagonCards(List.of(wagonCard, wagonCard, wagonCard));
        verify(gameModel).discardWagonCards(List.of(wagonCard, wagonCard, wagonCard));


        when(gameModel.discardWagonCards(anyList())).thenReturn(false);
        assertFalse(wagonCardsController.resetPlayer(player));
        verify(player, times(2)).getWagonCardsHand();
        verify(player, times(2)).removeWagonCards(List.of(wagonCard, wagonCard, wagonCard));
        verify(gameModel, times(2)).discardWagonCards(List.of(wagonCard, wagonCard, wagonCard));
    }

    @Test
    void testResetGame() {
        when(gameModel.clearWagonCardsDeck()).thenReturn(true);
        assertTrue(wagonCardsController.resetGame());
        verify(gameModel).clearWagonCardsDeck();

        when(gameModel.clearWagonCardsDeck()).thenReturn(false);
        assertFalse(wagonCardsController.resetGame());
        verify(gameModel, times(2)).clearWagonCardsDeck();
    }
}