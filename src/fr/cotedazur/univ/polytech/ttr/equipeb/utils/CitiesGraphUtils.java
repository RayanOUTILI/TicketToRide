package fr.cotedazur.univ.polytech.ttr.equipeb.utils;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;

import java.util.*;

public class CitiesGraphUtils {

    private CitiesGraphUtils() {
        throw new IllegalStateException("Utility class");
    }

    // This method creates the description of a graph of cities and their connected cities
    // Basically, The City is the key that points to another city that goes to the length of the route
    // The graph is bidirectional
    // City -> City -> Length
    public static Map<City, Map<City, Integer>> getGraphFromRoutes(List<RouteReadOnly> routes){
        Map<City, Map<City, Integer>> graph = new HashMap<>();
        routes.forEach(route -> {
            City firstCity = route.getFirstCity();
            City secondCity = route.getSecondCity();
            graph.putIfAbsent(firstCity, new HashMap<>());
            graph.putIfAbsent(secondCity, new HashMap<>());
            graph.get(firstCity).put(secondCity, route.getLength());
            graph.get(secondCity).put(firstCity, route.getLength());
        });
        return graph;
    }

    // This method is used to find the lengths between all cities
    // Also, if the graph is not connected, it will find the lengths between all connected components
    public static Set<CityPair> findLengthBetweenAllCityInGraph(Map<City, Map<City, Integer>> graph) {
        Set<CityPair> allCityPairs = new HashSet<>();
        for (City city : graph.keySet()) {
            findLengthBetweenAllCityInGraph(city, city, 0, graph, allCityPairs, new HashSet<>());
        }
        return allCityPairs;
    }

    // This method is used for the recursive search of the length between all cities
    private static void findLengthBetweenAllCityInGraph(
            City start,
            City current,
            int currentPathLength,
            Map<City, Map<City, Integer>> graph,
            Set<CityPair> allCityPairs,
            Set<CityPair> visitedNeighborsPairs
    ) {
        for (City neighbor : graph.getOrDefault(current, new HashMap<>()).keySet()) {
            CityPair pairCurrentToNeighbor = new CityPair(current, neighbor);
            CityPair pairStartToNeighbor = new CityPair(start, neighbor);
            // We check if the pair current to neighbor is not already in the set
            // because if it is, this means that we already use the route,
            // so we avoid infinite loops
            if (!visitedNeighborsPairs.contains(pairCurrentToNeighbor)) {
                // Calc the length from start to neighbor
                int lengthStartToNeighbor = currentPathLength + graph.get(current).get(neighbor);

                // We get the object in the set
                Optional<CityPair> optionalPair = allCityPairs.stream()
                        .filter(pair -> pair.equals(pairStartToNeighbor))
                        .findFirst();

                // If the pair is already in the set, we update the min and max length
                if (optionalPair.isPresent()) {
                    CityPair currentPairInSet = optionalPair.get();
                    currentPairInSet.setMinLength(Math.min(currentPairInSet.getMinLength(), lengthStartToNeighbor));
                    currentPairInSet.setMaxLength(Math.max(currentPairInSet.getMaxLength(), lengthStartToNeighbor));
                    // Else we add the pair to the set
                } else {
                    allCityPairs.add(pairStartToNeighbor);
                    pairStartToNeighbor.setMinLength(lengthStartToNeighbor);
                    pairStartToNeighbor.setMaxLength(lengthStartToNeighbor);
                }

                // We add the pair to avoid infinite loops
                visitedNeighborsPairs.add(pairCurrentToNeighbor);
                // We continue to find the length between all cities
                findLengthBetweenAllCityInGraph(start, neighbor, lengthStartToNeighbor, graph, allCityPairs, new HashSet<>(visitedNeighborsPairs));
            }
        }
    }
}
