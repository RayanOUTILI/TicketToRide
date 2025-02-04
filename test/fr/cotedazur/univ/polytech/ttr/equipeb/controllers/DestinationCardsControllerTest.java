package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.LongDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        List<LongDestinationCard> longDestCards = List.of(mock(LongDestinationCard.class));
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(gameModel.drawLongDestinationCards(1)).thenReturn(longDestCards);
        assertTrue(destinationCardsController.initPlayer(player));
        verify(player).receivedDestinationCards(cards);
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
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
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
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        List<ShortDestinationCard> emptyList = new ArrayList<>();
        when(player.askDestinationCards(cards)).thenReturn(emptyList);
        Optional<ReasonActionRefused> action = destinationCardsController.doAction(player);
        assertTrue(action.isPresent());
        assertEquals(ReasonActionRefused.DESTINATION_CARDS_CHOSEN_CARDS_EMPTY, action.get());
        verify(gameModel).returnShortDestinationCardsToTheBottom(cards);
    }

    @org.junit.jupiter.api.Test
    void testCompleted() {
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.isShortDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.isLongDestCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(player.askDestinationCards(cards)).thenReturn(cards);
        Optional<ReasonActionRefused> action = destinationCardsController.doAction(player);
        assertTrue(action.isEmpty());
        verify(player).receivedDestinationCards(cards);
    }

}