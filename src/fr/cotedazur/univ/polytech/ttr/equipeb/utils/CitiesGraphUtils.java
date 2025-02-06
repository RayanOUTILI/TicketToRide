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

    /**
     * This method is used to find the shortest path between two cities
     * Used the Dijkstra algorithm
     *
     * @param graph  the graph of cities and their connected cities
     * @param start  the start city
     * @param finish the finish city
     * @return the shortest path between the two cities
     */
    public static List<RouteReadOnly> findShortestPathBetweenCities(Map<City, Map<City, Integer>> graph, List<RouteReadOnly> allRoutes, City start, City finish) {
        Map<City, Integer> distances = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        PriorityQueue<City> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<City> visited = new HashSet<>();

        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            City current = queue.poll();
            if (current.equals(finish)) {
                break;
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (City neighbor : graph.getOrDefault(current, new HashMap<>()).keySet()) {
                int newDistance = distances.get(current) + graph.get(current).get(neighbor);
                if (!distances.containsKey(neighbor) || newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<RouteReadOnly> path = new ArrayList<>();
        City current = finish;
        while (previous.containsKey(current)) {
            City previousCity = previous.get(current);
            City finalCurrent = current;
            path.add(allRoutes.stream()
                    .filter(route -> route.getFirstCity().equals(previousCity) && route.getSecondCity().equals(finalCurrent))
                    .findFirst()
                    .orElseThrow());
            current = previousCity;
        }
        Collections.reverse(path);
        return path;
    }
}
