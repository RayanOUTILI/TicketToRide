package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelControllable;

import java.util.*;

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

    private void calculateDestinationCardScore(IPlayerModelControllable player) {
        List<RouteReadOnly> claimedRoutes = gameModel.getAllRoutesClaimedByPlayer(player.getIdentification());
        List<DestinationCard> destinationCards = player.getDestinationCardsHand();

        // Create a graph of cities and their connected cities
        // This will allow us to find all pairs of cities with continuous routes
        // At first, we will consider all routes as bidirectional
        Map<City, Map<City, Integer>> graph = new HashMap<>();
        claimedRoutes.forEach(route -> {
            City firstCity = route.getFirstCity();
            City secondCity = route.getSecondCity();
            graph.putIfAbsent(firstCity, new HashMap<>());
            graph.putIfAbsent(secondCity, new HashMap<>());
            graph.get(firstCity).put(secondCity, route.getLength());
            graph.get(secondCity).put(firstCity, route.getLength());
        });

        // Find all pairs of cities with continuous routes
        Set<CityPair> allCityPairs = new HashSet<>();
        Set<CityPair> visitedPairs = new HashSet<>();
        graph.keySet().forEach(
                city -> findAllCityPairs(city, city, 0, new HashSet<>(), graph, allCityPairs, visitedPairs)
        );

        // Calculate the score based on destination cards
        int score = destinationCards.stream()
                .mapToInt(card -> {
                    CityPair pair = new CityPair(card.getStartCity(), card.getEndCity());
                    return allCityPairs.contains(pair) ?
                            card.getPoints() : -card.getPoints();
                })
                .sum();

        System.out.println("All city pairs: " + allCityPairs);
        System.out.println("Max lenght route: " + allCityPairs.stream().max(Comparator.comparingInt(CityPair::getLength)));

        player.setScore(player.getScore() + score);
    }

    private void findAllCityPairs(
            City start,
            City current,
            int currentPathLength,
            Set<City> visited,
            Map<City, Map<City, Integer>> graph,
            Set<CityPair> allCityPairs,
            Set<CityPair> visitedPairs
    ) {
        visited.add(current);
        for (City neighbor : graph.getOrDefault(current, new HashMap<>()).keySet()) {
            int length = currentPathLength + graph.get(current).get(neighbor);
            CityPair pair = new CityPair(start, neighbor, length);
            if (!visitedPairs.contains(pair)) {
                allCityPairs.add(pair);
                visitedPairs.add(pair);
                if (!visited.contains(neighbor)) {
                    findAllCityPairs(start, neighbor, length, visited, graph, allCityPairs, visitedPairs);
                }
            }
        }
    }

    public void calculateFinalScores(){
        gameModel.getPlayers().forEach(this::updateScore);
        gameModel.getPlayers().forEach(this::calculateDestinationCardScore);
    }
}
