package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame.EndGameReasons;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IVictoryControllerGameModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VictoryControllerTest {

    private VictoryController victoryController;
    private IVictoryControllerGameModel gameModel;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gameModel = mock(IVictoryControllerGameModel.class);
        victoryController = new VictoryController(gameModel);
    }


    @org.junit.jupiter.api.Test
    void endGameNull() {
        when(gameModel.isAllRoutesClaimed()).thenReturn(false);
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);

        assertNull(victoryController.endGame());
    }

    @org.junit.jupiter.api.Test
    void endGameAllRoutesClaimed() {
        when(gameModel.isAllRoutesClaimed()).thenReturn(true);
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(false);

        assertEquals(EndGameReasons.ALL_ROUTES_CLAIMED, victoryController.endGame().reason());
    }

    @org.junit.jupiter.api.Test
    void endGameEmptyWagonCardsDeck() {
        when(gameModel.isAllRoutesClaimed()).thenReturn(false);
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(true);

        assertEquals(EndGameReasons.EMPTY_WAGON_CARDS_DECK, victoryController.endGame().reason());
    }

    @org.junit.jupiter.api.Test
    void endTurn() {
        when(gameModel.isWagonCardDeckEmpty()).thenReturn(true);
        victoryController.endTurn();
        verify(gameModel).fillWagonCardDeck();
    }

}