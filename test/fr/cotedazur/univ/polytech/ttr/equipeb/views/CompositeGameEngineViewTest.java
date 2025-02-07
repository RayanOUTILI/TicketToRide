package fr.cotedazur.univ.polytech.ttr.equipeb.views;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CompositeGameEngineViewTest {
    private CompositeGameEngineView compositeGameEngineView;
    private IGameViewable mockView;

    @BeforeEach
    void setup() {
        compositeGameEngineView = new CompositeGameEngineView();
        mockView = mock(IGameViewable.class);
    }

    @Test
    void testDisplayNewGameWithEmptyList() {
        assertDoesNotThrow(() -> compositeGameEngineView.displayNewGame());
        verify(mockView, never()).displayNewGame();
    }

    @Test
    void testDisplayNewGameWithOneElement() {
        compositeGameEngineView.addView(mockView);
        assertDoesNotThrow(() -> compositeGameEngineView.displayNewGame());
        verify(mockView, times(1)).displayNewGame();
    }

    @Test
    void testDisplayNewTurnWithEmptyList() {
        assertDoesNotThrow(() -> compositeGameEngineView.displayNewTurn(1));
        verify(mockView, never()).displayNewTurn(1);
    }

    @Test
    void testDisplayNewTurnWithOneElement() {
        compositeGameEngineView.addView(mockView);
        assertDoesNotThrow(() -> compositeGameEngineView.displayNewTurn(1));
        verify(mockView, times(1)).displayNewTurn(1);
    }

    @Test
    void testDisplayEndGameReasonWithEmptyList() {
        assertDoesNotThrow(() -> compositeGameEngineView.displayEndGameReason(PlayerIdentification.GREEN, 0, 10));
        verify(mockView, never()).displayEndGameReason(PlayerIdentification.GREEN, 0, 10);
    }

    @Test
    void testDisplayEndGameReasonWithOneElement() {
        compositeGameEngineView.addView(mockView);
        assertDoesNotThrow(() -> compositeGameEngineView.displayEndGameReason(PlayerIdentification.GREEN, 0, 10));
        verify(mockView, times(1)).displayEndGameReason(PlayerIdentification.GREEN, 0, 10);
    }

    @Test
    void testDisplayWinnerWithEmptyList() {
        assertDoesNotThrow(() -> compositeGameEngineView.displayWinner(PlayerIdentification.GREEN, 100));
        verify(mockView, never()).displayWinner(PlayerIdentification.GREEN, 100);
    }

    @Test
    void testDisplayWinnerWithOneElement() {
        compositeGameEngineView.addView(mockView);
        assertDoesNotThrow(() -> compositeGameEngineView.displayWinner(PlayerIdentification.GREEN, 100));
        verify(mockView, times(1)).displayWinner(PlayerIdentification.GREEN, 100);
    }
}