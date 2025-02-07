package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class PlayerModelTest {
    
    private PlayerModel playerModel;
    private IPlayerViewable playerViewable;

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
        playerViewable = mock(IPlayerViewable.class);
        playerModel = new PlayerModel(PlayerIdentification.BLUE, playerViewable);
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

    @Test
    void testReceiveDestinationCards() {
        DestinationCard destinationCard = mock(DestinationCard.class);
        List<DestinationCard> destinationCards = List.of(destinationCard);

        playerModel.receiveDestinationCards(destinationCards);
        assertEquals(1, playerModel.getDestinationCards().size());
        verify(playerViewable, times(1)).displayReceivedDestinationCards(destinationCards);
    }

    @Test
    void testNotifyClaimedRoute() {
        RouteReadOnly route = mock(RouteReadOnly.class);

        playerModel.notifyClaimedRoute(route);
        verify(playerViewable, times(1)).displayClaimedRoute(route);
    }

    @Test
    void testNotifyClaimedStation() {
        CityReadOnly city = mock(CityReadOnly.class);
        List<WagonCard> wagonCards = List.of(mock(WagonCard.class));

        playerModel.notifyClaimedStation(city, wagonCards);
        verify(playerViewable, times(1)).displayClaimedStation(city, wagonCards, playerModel.getStationsLeft());
    }

    @Test
    void testSetScore() {
        playerModel.setScore(10);
        assertEquals(10, playerModel.getScore());
        verify(playerViewable, times(1)).displayNewScore(10);
    }

    @Test
    void testSetScoreNoChange() {
        playerModel.setScore(0);
        verify(playerViewable, never()).displayNewScore(anyInt());
    }

    @Test
    void testClearMethods() {
        playerModel.clearDestinationCards();
        assertTrue(playerModel.getDestinationCards().isEmpty());
        assertTrue(playerModel.getDiscardDestinationCards().isEmpty());

        playerModel.clearChosenRouteStations();
        assertTrue(playerModel.getSelectedStationRoutes().isEmpty());

        playerModel.clearScore();
        assertEquals(0, playerModel.getScore());
        assertEquals(0, playerModel.getLongestContinuousRouteLength());
        assertEquals(0, playerModel.getNumberOfCompletedObjectiveCards());

        playerModel.clearStationsLeft();
        assertEquals(0, playerModel.getStationsLeft());

        playerModel.clearNumberOfWagons();
        assertEquals(0, playerModel.getNumberOfWagons());
    }

    @Test
    void testIncrementNumberOfCompletedObjectiveCards() {
        playerModel.incrementNumberOfCompletedObjectiveCards(5);
        assertEquals(5, playerModel.getNumberOfCompletedObjectiveCards());
    }

    @Test
    void testSetLongestContinuousRouteLength() {
        playerModel.setLongestContinuousRouteLength(15);
        assertEquals(15, playerModel.getLongestContinuousRouteLength());
    }

    @Test
    void testDiscardDestinationCard() {
        DestinationCard destinationCard = mock(DestinationCard.class);
        List<DestinationCard> destinationCards = List.of(destinationCard);

        playerModel.discardDestinationCard(destinationCards);
        assertEquals(1, playerModel.getDiscardDestinationCards().size());
    }

    @Test
    void testRemoveWagonCardsFailure() {
        WagonCard wagonCard = mock(WagonCard.class);
        List<WagonCard> result = playerModel.removeWagonCards(List.of(wagonCard));
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetWagonCardsHand() {
        WagonCard wagonCard = mock(WagonCard.class);
        playerModel.receivedWagonCard(wagonCard);
        List<WagonCard> hand = playerModel.getWagonCardsHand();
        assertEquals(1, hand.size());
        assertEquals(wagonCard, hand.getFirst());
    }

    @Test
    void testReplaceRemovedWagonCards() {
        WagonCard wagonCard = mock(WagonCard.class);
        playerModel.replaceRemovedWagonCards(List.of(wagonCard));
        assertEquals(1, playerModel.getNumberOfWagonCards());
    }

    @Test
    void testDefineStartingStationsNumber() {
        playerModel.defineStartingStationsNumber(5);
        assertEquals(5, playerModel.getStationsLeft());
    }

    @Test
    void testDecrementStationsLeft() {
        playerModel.defineStartingStationsNumber(5);
        playerModel.decrementStationsLeft();
        assertEquals(4, playerModel.getStationsLeft());
    }

    @Test
    void testSetNumberOfWagons() {
        playerModel.setNumberOfWagons(45);
        assertEquals(45, playerModel.getNumberOfWagons());
    }

    @Test
    void testRemoveWagons() {
        playerModel.setNumberOfWagons(45);
        playerModel.removeWagons(5);
        assertEquals(40, playerModel.getNumberOfWagons());
    }

    @Test
    void testAddChosenRouteStation() {
        RouteReadOnly route = mock(RouteReadOnly.class);
        playerModel.addChosenRouteStation(route);
        assertEquals(1, playerModel.getSelectedStationRoutes().size());
        assertEquals(route, playerModel.getSelectedStationRoutes().getFirst());
    }

    @Test
    void testGetWagonCardsOfColor() {
        WagonCard blueCard = mock(WagonCard.class);
        when(blueCard.getColor()).thenReturn(Color.BLUE);
        playerModel.receivedWagonCard(blueCard);
        List<WagonCard> cards = playerModel.getWagonCardsOfColor(Color.BLUE, 1);
        assertEquals(1, cards.size());
        assertEquals(blueCard, cards.getFirst());
    }

    @Test
    void testGetNumberOfWagonCardsIncludingAnyColor() {
        WagonCard blueCard = mock(WagonCard.class);
        when(blueCard.getColor()).thenReturn(Color.BLUE);
        WagonCard anyCard = mock(WagonCard.class);
        when(anyCard.getColor()).thenReturn(Color.ANY);
        playerModel.receivedWagonCards(List.of(blueCard, anyCard));
        int count = playerModel.getNumberOfWagonCardsIncludingAnyColor(Color.BLUE);
        assertEquals(2, count);
    }

    @Test
    void testGetPlayerType() {
        assertEquals(PlayerType.EASY_BOT, playerModel.getPlayerType());
    }
}