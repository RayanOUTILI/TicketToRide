package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IStationControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StationControllerTest {

    private IStationControllerGameModel gameModel;
    private StationController stationController;
    private Player player;
    private ClaimObject<CityReadOnly> claimStation;
    private City city;
    private WagonCard wagonCard;

    @BeforeEach
    void setUp() {
        gameModel = mock(IStationControllerGameModel.class);
        stationController = new StationController(gameModel);
        player = mock(Player.class);
        when(player.getIdentification()).thenReturn(PlayerIdentification.BLUE);
        claimStation = mock(ClaimObject.class);
        city = mock(City.class);
        wagonCard = mock(WagonCard.class);
    }

    @Test
    void initGameReturnsTrue() {
        assertTrue(stationController.initGame());
    }

    @Test
    void initPlayerSetsStartingStations() {
        stationController.initPlayer(player);
        verify(player).defineStartingStationsNumber(3);
    }

    @Test
    void doActionReturnsStationNotEnoughStationsLeftWhenNoStationsLeft() {
        when(player.getStationsLeft()).thenReturn(0);
        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertEquals(Optional.of(ReasonActionRefused.STATION_NOT_ENOUGH_STATIONS_LEFT), result);
    }

    @Test
    void doActionReturnsStationCityNotValidWhenCityIsNull() {
        when(player.getStationsLeft()).thenReturn(1);
        when(player.askClaimStation()).thenReturn(claimStation);
        when(claimStation.getClaimable()).thenReturn(null);
        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertEquals(Optional.of(ReasonActionRefused.STATION_CITY_NOT_VALID), result);
    }

    @Test
    void doActionReturnsStationWrongWagonCardsColorWhenCardsHaveDifferentColors() {
        when(player.getStationsLeft()).thenReturn(3);
        when(player.askClaimStation()).thenReturn(claimStation);
        when(claimStation.getClaimable()).thenReturn(city);
        when(gameModel.getCity(anyInt())).thenReturn(city);
        when(city.isClaimed()).thenReturn(false);
        when(claimStation.wagonCards()).thenReturn(List.of(wagonCard));
        when(player.removeWagonCards(anyList())).thenReturn(List.of(wagonCard));
        when(wagonCard.getColor()).thenReturn(Color.RED).thenReturn(Color.BLUE);

        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertEquals(Optional.of(ReasonActionRefused.STATION_WRONG_WAGON_CARDS_COLOR), result);
    }

    @Test
    void doActionEmptyWagonCards() {
        when(player.getStationsLeft()).thenReturn(1);
        when(player.askClaimStation()).thenReturn(claimStation);
        when(claimStation.getClaimable()).thenReturn(city);
        when(gameModel.getCity(anyInt())).thenReturn(city);
        when(city.isClaimed()).thenReturn(false);
        when(claimStation.wagonCards()).thenReturn(List.of());
        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertTrue(result.isPresent());
        assertEquals(ReasonActionRefused.STATION_NOT_ENOUGH_WAGON_CARDS, result.get());
        verify(player, times(1)).replaceRemovedWagonCards(anyList());
    }

    @Test
    void doActionEmptyResult() {
        when(player.getStationsLeft()).thenReturn(3);
        when(player.askClaimStation()).thenReturn(claimStation);
        when(claimStation.getClaimable()).thenReturn(city);
        when(gameModel.getCity(anyInt())).thenReturn(city);
        when(city.isClaimed()).thenReturn(false);
        when(wagonCard.getColor()).thenReturn(Color.RED);
        List<WagonCard> givenWagonCards = new ArrayList<>(List.of(wagonCard));
        when(claimStation.wagonCards()).thenReturn(givenWagonCards);
        List<WagonCard> removedCards = new ArrayList<>(List.of(wagonCard));
        when(player.removeWagonCards(anyList())).thenReturn(removedCards);


        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertTrue(result.isEmpty());
        verify(gameModel, times(1)).discardWagonCards(removedCards);
        verify(player, times(1)).decrementStationsLeft();
        verify(city, times(1)).setOwner(player.getIdentification());
        verify(player, times(1)).notifyClaimedStation(city, removedCards);
    }

    @Test
    void testResetPlayer() {
        when(player.clearStationsLeft()).thenReturn(true);
        assertTrue(stationController.resetPlayer(player));

        when(player.clearStationsLeft()).thenReturn(false);
        assertFalse(stationController.resetPlayer(player));
    }

    @Test
    void testResetGame() {
        when(gameModel.setAllStationsNotClaimed()).thenReturn(true);
        assertTrue(stationController.resetGame());

        when(gameModel.setAllStationsNotClaimed()).thenReturn(false);
        assertFalse(stationController.resetGame());
    }
}