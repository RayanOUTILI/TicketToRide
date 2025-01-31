package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimStation;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IStationControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StationControllerTest {

    private IStationControllerGameModel gameModel;
    private StationController stationController;
    private Player player;
    private ClaimStation claimStation;
    private City city;
    private WagonCard wagonCard;

    @BeforeEach
    void setUp() {
        gameModel = mock(IStationControllerGameModel.class);
        stationController = new StationController(gameModel);
        player = mock(Player.class);
        claimStation = mock(ClaimStation.class);
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
        when(claimStation.city()).thenReturn(null);
        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertEquals(Optional.of(ReasonActionRefused.STATION_CITY_NOT_VALID), result);
    }

    @Test
    void doActionReturnsStationWrongWagonCardsColorWhenCardsHaveDifferentColors() {
        when(player.getStationsLeft()).thenReturn(3);
        when(player.askClaimStation()).thenReturn(claimStation);
        when(claimStation.city()).thenReturn(city);
        when(gameModel.getCity(anyInt())).thenReturn(city);
        when(city.isClaimed()).thenReturn(false);
        when(claimStation.wagonCards()).thenReturn(List.of(wagonCard));
        when(player.removeWagonCards(anyList())).thenReturn(List.of(wagonCard));
        when(wagonCard.getColor()).thenReturn(Color.RED).thenReturn(Color.BLUE);

        Optional<ReasonActionRefused> result = stationController.doAction(player);
        assertEquals(Optional.of(ReasonActionRefused.STATION_WRONG_WAGON_CARDS_COLOR), result);
    }
}