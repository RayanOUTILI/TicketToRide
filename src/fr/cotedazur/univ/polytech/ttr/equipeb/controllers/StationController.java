package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimObject;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IStationControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.CityReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.List;
import java.util.Optional;

public class StationController extends Controller{

    private static final int STARTING_STATIONS = 3;
    private final IStationControllerGameModel gameModel;

    public StationController(IStationControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    // Get the city from the Claim Station Object
    // If the city is null or already claimed, return null
    private City checkCityAvailable(ClaimObject<CityReadOnly> wantedCity) {
        if(wantedCity == null || wantedCity.getClaimable() == null) return null;
        City city = gameModel.getCity(wantedCity.getClaimable().getId());
        return city != null && !city.isClaimed() ? city : null;
    }

    @Override
    public boolean initGame() {
        return true;
    }

    @Override
    public boolean initPlayer(Player player) {
        player.defineStartingStationsNumber(STARTING_STATIONS);
        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {

        int stationsLeft = player.getStationsLeft();
        if (stationsLeft <= 0) return Optional.of(ReasonActionRefused.STATION_NOT_ENOUGH_STATIONS_LEFT);

        ClaimObject<CityReadOnly> claimStation = player.askClaimStation();
        City city = checkCityAvailable(claimStation);

        if (city == null) return Optional.of(ReasonActionRefused.STATION_CITY_NOT_VALID);

        List<WagonCard> wagonCards = claimStation.wagonCards();

        List<WagonCard> removedCards = player.removeWagonCards(wagonCards);

        // Check if the player selected the right amount of cards
        // Basically, when it's your first time claiming, you must select 1 card
        // Then, you must select 2 cards, and so on
        // The cards has to be the same color
        if (removedCards.size() != getNumberOfRequiredCards(stationsLeft)) {
            player.replaceRemovedWagonCards(removedCards);
            return Optional.of(ReasonActionRefused.STATION_NOT_ENOUGH_WAGON_CARDS);
        }

        // Check if the cards are the same color
        WagonCard firstCard = removedCards.getFirst();
        boolean allCardsHaveSameColor = removedCards.stream().allMatch(
                card -> card.getColor() == firstCard.getColor()
                        || card.getColor() == Color.ANY
        );

        if (!allCardsHaveSameColor) {
            player.replaceRemovedWagonCards(removedCards);
            return Optional.of(ReasonActionRefused.STATION_WRONG_WAGON_CARDS_COLOR);
        }

        gameModel.discardWagonCards(removedCards);

        player.decrementStationsLeft();
        city.setOwner(player.getIdentification());

        player.notifyClaimedStation(city, removedCards);

        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        return player.clearStationsLeft();
    }

    @Override
    public boolean resetGame() {
        return gameModel.setAllStationsNotClaimed();
    }

    private int getNumberOfRequiredCards(int stationsLeft) {
        return STARTING_STATIONS - (stationsLeft - 1);
    }
}
