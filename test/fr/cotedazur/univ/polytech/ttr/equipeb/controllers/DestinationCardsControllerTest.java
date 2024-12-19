package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IDestinationCardsControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void testInit() {
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(false);
        when(gameModel.shuffleDestinationCardDeck()).thenReturn(true);
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        assertTrue(destinationCardsController.init(player));
        verify(player).receivedDestinationCards(cards);
    }

    @org.junit.jupiter.api.Test
    void testWithEmptyDeck() {
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(true);
        assertFalse(destinationCardsController.doAction(player));
    }

    @org.junit.jupiter.api.Test
    void testWithNullChosenCards() {
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(player.askDestinationCards(cards)).thenReturn(null);
        assertFalse(destinationCardsController.doAction(player));
        verify(gameModel).returnDestinationCardsToTheBottom(cards);
    }

    @org.junit.jupiter.api.Test
    void testWithEmptyChosenCards() {
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        List<ShortDestinationCard> emptyList = new ArrayList<>();
        when(player.askDestinationCards(cards)).thenReturn(emptyList);
        assertFalse(destinationCardsController.doAction(player));
        verify(gameModel).returnDestinationCardsToTheBottom(cards);
    }

    @org.junit.jupiter.api.Test
    void testCompleted() {
        List<ShortDestinationCard> cards = List.of(mock(ShortDestinationCard.class), mock(ShortDestinationCard.class), mock(ShortDestinationCard.class));
        when(gameModel.isDestinationCardDeckEmpty()).thenReturn(false);
        when(gameModel.drawDestinationCards(3)).thenReturn(cards);
        when(player.askDestinationCards(cards)).thenReturn(cards);
        assertTrue(destinationCardsController.doAction(player));
        verify(player).receivedDestinationCards(cards);
    }

}