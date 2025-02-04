package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class DestinationCardsControllerTest {
    private DestinationCardsController destinationCardsController;
    private IDestinationCardsControllerGameModel gameModel;
    private Player player;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IDestinationCardsControllerGameModel.class);
        destinationCardsController = new DestinationCardsController(gameModel);
        player = mock(Player.class);
    }

    @Test
    void testInitPlayer() {
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleDestinationCardsDecks()).thenReturn(true);
        List<DestinationCard> cards = List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class));
        List<DestinationCard> longDestCards = List.of(mock(DestinationCard.class));

        List<DestinationCard> allCards = new ArrayList<>(cards);
        allCards.addAll(longDestCards);

        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(gameModel.drawLongDestinationCards(1)).thenReturn(longDestCards);
        when(player.askInitialDestinationCards(allCards)).thenReturn(allCards);
        assertTrue(destinationCardsController.initPlayer(player));;
        verify(player).receiveDestinationCards(allCards);
    }

    @org.junit.jupiter.api.Test
    void testWithEmptyDeck() {
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(true);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(true);
        Optional<ReasonActionRefused> reason = Optional.of(ReasonActionRefused.DESTINATION_CARDS_DECK_EMPTY);
        assertEquals(destinationCardsController.doAction(player), reason);
    }

    @org.junit.jupiter.api.Test
    void testWithNullChosenCards() {
        List<DestinationCard> cards = List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class));
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(player.askDestinationCards(cards)).thenReturn(null);
        Optional<ReasonActionRefused> action = destinationCardsController.doAction(player);
        assertTrue(action.isPresent());
        assertEquals(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY, action.get());
        verify(gameModel).returnShortDestinationCardsToTheBottom(cards);
    }

    @org.junit.jupiter.api.Test
    void testWithEmptyChosenCards() {
        List<DestinationCard> cards = List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class));
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        List<DestinationCard> emptyList = new ArrayList<>();
        when(player.askDestinationCards(cards)).thenReturn(emptyList);
        Optional<ReasonActionRefused> action = destinationCardsController.doAction(player);
        assertTrue(action.isPresent());
        assertEquals(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY, action.get());
        verify(gameModel).returnShortDestinationCardsToTheBottom(cards);
    }

    @org.junit.jupiter.api.Test
    void testCompleted() {
        List<DestinationCard> cards = List.of(mock(DestinationCard.class), mock(DestinationCard.class), mock(DestinationCard.class));
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(player.askDestinationCards(cards)).thenReturn(cards);
        Optional<ReasonActionRefused> action = destinationCardsController.doAction(player);
        assertTrue(action.isEmpty());
        verify(player).receiveDestinationCards(cards);
    }

    @Test
    void testResetPlayer() {
        when(player.clearDestinationCards()).thenReturn(true);
        assertTrue(destinationCardsController.resetPlayer(player));

        when(player.clearDestinationCards()).thenReturn(false);
        assertFalse(destinationCardsController.resetPlayer(player));
    }

    @Test
    void testResetGame() {
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(true);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(true);
        assertFalse(destinationCardsController.resetGame());

        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(true);
        assertFalse(destinationCardsController.resetGame());

        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(true);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        assertFalse(destinationCardsController.resetGame());

        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        assertTrue(destinationCardsController.resetGame());
    }

    @Test
    void testInitGame() {
        when(gameModel.shuffleDestinationCardsDecks()).thenReturn(true);
        assertTrue(destinationCardsController.initGame());

        when(gameModel.shuffleDestinationCardsDecks()).thenReturn(false);
        assertFalse(destinationCardsController.initGame());
    }

}