package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

import java.util.*;
import java.util.stream.Collectors;

import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.findLengthBetweenAllCityInGraph;
import static fr.cotedazur.univ.polytech.ttr.equipeb.utils.CitiesGraphUtils.getGraphFromRoutes;

public class ScoreController {
    IScoreControllerGameModel gameModel;

    public ScoreController( IScoreControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void updateScore(IPlayerModelControllable player) {
        int score = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())
                            .stream()
                            .mapToInt(route -> switch (route.getLength()) {
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 4;
                                case 4 -> 7;
                                case 6 -> 15;
                                case 8 -> 21;
                                default -> 0;
                            })
                            .sum();
        player.setScore(score);
    }

    private void calculateDestinationCardsScore(IPlayerModelControllable player) {
        // TODO: Here for the station implementation
        // Its necessary to remove the length of routes that is "claimed" with stations
        // Please note that the station implementation is not yet implemented here
        List<DestinationCard> destinationCards = player.getDestinationCardsHand();
        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification());

        // Find all pairs of cities with continuous routes
        Map<City, Map<City, Integer>> claimedPlayerRouteGraph = getGraphFromRoutes(claimedRoutes);
        Set<CityPair> allCityPairs = findLengthBetweenAllCityInGraph(claimedPlayerRouteGraph);

        // Calculate the score based on destination cards
        int score = destinationCards.stream()
                .mapToInt(card -> {
                    CityPair pair = new CityPair(card.getStartCity(), card.getEndCity());
                    if (allCityPairs.contains(pair)) {
                        //TODO: View
                        System.out.println("Player {" + player.getIdentification() + "} has a destination between " + card.getStartCity() + " and " + card.getEndCity() + " (" + card.getPoints() + " points)");
                        return card.getPoints();
                    } else {
                        System.out.println("Player {" + player.getIdentification() + "} does not have a destination between " + card.getStartCity() + " and " + card.getEndCity() + " (" + -card.getPoints() + " points)");
                        return -card.getPoints();
                    }
                })
                .sum();

        player.setScore(player.getScore() + score);
    }

    private void calculateRemainingStationsScore(IPlayerModelControllable player) {
        player.setScore(player.getScore() - player.getStationsLeft() * 4);
    }

    private void calculatePlayerWithTheLongestContinuousRoute() {
        Map<IPlayerModelControllable, Integer> playerLongestPaths = gameModel.getPlayers().stream()
                .map(player -> {
                    List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification());
                    Set<CityPair> allCityPairs = findLengthBetweenAllCityInGraph(getGraphFromRoutes(claimedRoutes));
                    int longestPath = allCityPairs.stream()
                            .mapToInt(CityPair::getMaxLength)
                            .max()
                            .orElse(0);
                    return new AbstractMap.SimpleEntry<>(player, longestPath);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int maxLongestPath = Collections.max(playerLongestPaths.values());

        List<IPlayerModelControllable> playersWithLongestPath = playerLongestPaths.entrySet().stream()
                .filter(entry -> entry.getValue() == maxLongestPath)
                .map(Map.Entry::getKey)
                .toList();

        //TODO: View
        playersWithLongestPath.forEach(
                player -> System.out.println("Player {" + player.getIdentification() + "} has the longest path of " + maxLongestPath)
        );

        playersWithLongestPath.forEach(player -> player.setScore(player.getScore() + 10));
    }

    public void calculateFinalScores(){
        gameModel.getPlayers().forEach(this::updateScore);
        gameModel.getPlayers().forEach(this::calculateDestinationCardsScore);
        gameModel.getPlayers().forEach(this::calculateRemainingStationsScore);
        calculatePlayerWithTheLongestContinuousRoute();
    }
}
