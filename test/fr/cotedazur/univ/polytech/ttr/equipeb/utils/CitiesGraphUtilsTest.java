package fr.cotedazur.univ.polytech.ttr.equipeb.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.ShortDestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;


class CitiesGraphUtilsTest {
    /*
    @Test
        //TODO: Test this in graph utils Test
    void testCalculateDestinationWithALoopCity() {
        ShortDestinationCard parisToBerlinDestination = new ShortDestinationCard(new City("Paris"), new City("Berlin"), 10);

        IPlayerModelControllable playerWithDestinations = new PlayerModel(PlayerIdentification.BLACK, null);
        playerWithDestinations.receivedDestinationCards(List.of(parisToBerlinDestination));

        Route routeParisToMadrid = new Route(new City("Paris"), new City("Madrid"), 5, RouteType.TRAIN, Color.BLACK , 5);
        routeParisToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToMarseille = new Route(new City("Madrid"), new City("Marseille"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToMarseille.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMarseilleToNice = new Route(new City("Marseille"), new City("Nice"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMarseilleToNice.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeNiceToMadrid = new Route(new City("Nice"), new City("Madrid"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeNiceToMadrid.setClaimerPlayer(PlayerIdentification.BLACK);

        Route routeMadridToBerlin = new Route(new City("Madrid"), new City("Berlin"), 5, RouteType.TRAIN, Color.BLACK, 5);
        routeMadridToBerlin.setClaimerPlayer(PlayerIdentification.BLACK);

        when(gameModel.getAllRoutesClaimedByPlayer(PlayerIdentification.BLACK)).thenReturn(List.of(routeMadridToBerlin, routeParisToMadrid, routeMadridToMarseille, routeMarseilleToNice, routeNiceToMadrid));
        when(gameModel.getPlayers()).thenReturn(List.of(playerWithDestinations));
        Set<CityPair> allClaimedCityPair = scoreController.getAllCityPairs(playerWithDestinations);
        System.out.println(allClaimedCityPair.size());
        assertEquals(13, allClaimedCityPair.size());

        System.out.println("All claimed city pairs:");
        for (CityPair pair : allClaimedCityPair) {
            System.out.println(pair);
        }

        // Check if the length of the route between Paris and Berlin is the highest of all possible routes
        // beetwen Paris and Berlin
        // it should be:
        // Paris -> Madrid -> Marseille -> Nice -> Madrid -> Berlin
        //        5    +    5     +     5   +   5     +    5        = 25

        Optional<CityPair> parisToBerlin = allClaimedCityPair.stream()
                .filter(pair -> pair.equals(new CityPair(new City("Paris"), new City("Berlin"))))
                .findFirst();

        assertTrue(parisToBerlin.isPresent());

        assertEquals(10, parisToBerlin.get().getMinLength());
        assertEquals(25, parisToBerlin.get().getMaxLength());
    }
    */
}