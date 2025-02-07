package fr.cotedazur.univ.polytech.ttr.equipeb.utils;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.score.CityPair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitiesGraphUtilsTest {
    //test for separated groups
    @Test
    void testLengthBetweenAllCityInGraphBasic() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");

        Map<City, Map<City, Integer>> graph = new HashMap<>();

        graph.put(nice, Map.of(marseille, 5));
        graph.put(marseille, Map.of(nice, 5, paris, 5));
        graph.put(paris, Map.of(marseille, 5));

        Set<CityPair> cityPairs =  CitiesGraphUtils.findLengthBetweenAllCityInGraph(graph);
        assertTrue(cityPairs.contains(new CityPair(nice, marseille)));
        assertTrue(cityPairs.contains(new CityPair(marseille, paris)));
        assertTrue(cityPairs.contains(new CityPair(nice, paris)));

        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, marseille))).findFirst().get().getMaxLength());
        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(marseille, paris))).findFirst().get().getMaxLength());
        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, paris))).findFirst().get().getMaxLength());
    }

    @Test
    void testLengthBetweenAllCityInGraphWithALoop() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");
        City lyon = new City("Lyon");

        Route niceMarseille = new Route(nice, marseille, 5, null, null, 0);
        Route marseilleParis = new Route(marseille, paris, 5, null, null, 0);
        Route marseilleLyon = new Route(marseille, lyon, 5, null, null, 0);
        Route lyonParis = new Route(lyon, paris, 5, null, null, 0);



        Map<City, Map<City, Integer>> graph = CitiesGraphUtils.getGraphFromRoutes(List.of(niceMarseille, marseilleParis, marseilleLyon, lyonParis));
        Set<CityPair> cityPairs =  CitiesGraphUtils.findLengthBetweenAllCityInGraph(graph);

        assertTrue(cityPairs.contains(new CityPair(nice, marseille)));
        assertTrue(cityPairs.contains(new CityPair(nice, paris)));
        assertTrue(cityPairs.contains(new CityPair(nice, lyon)));

        assertTrue(cityPairs.contains(new CityPair(marseille, paris)));
        assertTrue(cityPairs.contains(new CityPair(marseille, lyon)));

        assertTrue(cityPairs.contains(new CityPair(paris, lyon)));

        assertEquals(20, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, marseille))).findFirst().get().getMaxLength());
        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, marseille))).findFirst().get().getMinLength());

        assertEquals(15, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, paris))).findFirst().get().getMaxLength());
        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, paris))).findFirst().get().getMinLength());

        assertEquals(15, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, lyon))).findFirst().get().getMaxLength());
        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(nice, lyon))).findFirst().get().getMinLength());

        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(marseille, paris))).findFirst().get().getMaxLength());
        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(marseille, paris))).findFirst().get().getMinLength());

        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(marseille, lyon))).findFirst().get().getMaxLength());
        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(marseille, lyon))).findFirst().get().getMinLength());

        assertEquals(5, cityPairs.stream().filter(pair -> pair.equals(new CityPair(paris, lyon))).findFirst().get().getMinLength());
        assertEquals(10, cityPairs.stream().filter(pair -> pair.equals(new CityPair(paris, lyon))).findFirst().get().getMaxLength());
    }

    @Test
    void testGetGraphFromRoutesSeparatedGroups() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");

        City lyon = new City("Lyon");
        City bordeaux = new City("Bordeaux");
        City toulouse = new City("Toulouse");
        City montpellier = new City("Montpellier");


        Route niceMarseille = new Route(nice, marseille, 5, null, null, 0);
        Route marseilleParis = new Route(marseille, paris, 5, null, null, 0);

        Route lyonBordeaux = new Route(lyon, bordeaux, 5, null, null, 0);
        Route bordeauxToulouse = new Route(bordeaux, toulouse, 5, null, null, 0);
        Route toulouseMontpellier = new Route(toulouse, montpellier, 5, null, null, 0);


        List<RouteReadOnly> routes = List.of(niceMarseille, marseilleParis, lyonBordeaux, bordeauxToulouse, toulouseMontpellier);

        Map<City, Map<City, Integer>> graph = CitiesGraphUtils.getGraphFromRoutes(routes);

        assertEquals(7, graph.size());
        assertTrue(graph.containsKey(nice));
        assertTrue(graph.containsKey(marseille));
        assertTrue(graph.containsKey(paris));
        assertTrue(graph.containsKey(lyon));
        assertTrue(graph.containsKey(bordeaux));
        assertTrue(graph.containsKey(toulouse));
        assertTrue(graph.containsKey(montpellier));

        assertEquals(1, graph.get(nice).size());
        assertEquals(2, graph.get(marseille).size());
        assertEquals(1, graph.get(paris).size());

        assertEquals(1, graph.get(lyon).size());
        assertEquals(2, graph.get(bordeaux).size());
        assertEquals(2, graph.get(toulouse).size());
        assertEquals(1, graph.get(montpellier).size());
    }


    @Test
    void testGetGraphFromRoutes() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");
        City lyon = new City("Lyon");
        City bordeaux = new City("Bordeaux");

        // Nice -> Marseille -> Paris
        //          \-> Lyon
        //          \-> Bordeaux
        //                  \ -> Marseille

        Route niceMarseille = new Route(nice, marseille, 5, null, null, 0);
        Route marseilleParis = new Route(marseille, paris, 5, null, null, 0);
        Route marseilleLyon = new Route(marseille, lyon, 5, null, null, 0);
        Route lyonBordeaux = new Route(lyon, bordeaux, 5, null, null, 0);
        Route bordeauxMarseille = new Route(bordeaux, marseille, 5, null, null, 0);

        List<RouteReadOnly> routes = List.of(niceMarseille, marseilleParis, marseilleLyon, lyonBordeaux, bordeauxMarseille);

        Map<City, Map<City, Integer>> graph = CitiesGraphUtils.getGraphFromRoutes(routes);

        assertEquals(5, graph.size());
        assertTrue(graph.containsKey(nice));
        assertTrue(graph.containsKey(marseille));
        assertTrue(graph.containsKey(paris));
        assertTrue(graph.containsKey(lyon));
        assertTrue(graph.containsKey(bordeaux));

        assertEquals(1, graph.get(nice).size());
        assertEquals(4, graph.get(marseille).size());
        assertEquals(1, graph.get(paris).size());
        assertEquals(2, graph.get(lyon).size());
        assertEquals(2, graph.get(bordeaux).size());
    }

    @Test
    void testFindShortestPath1() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");
        City lyon = new City("Lyon");

        Route niceMarseille = new Route(nice, marseille, 5, null, null, 0);
        Route marseilleParis = new Route(marseille, paris, 12, null, null, 0);
        Route marseilleLyon = new Route(marseille, lyon, 5, null, null, 0);
        Route lyonParis = new Route(lyon, paris, 5, null, null, 0);


        List<RouteReadOnly> allRoutes = List.of(niceMarseille, marseilleParis, marseilleLyon, lyonParis);
        Map<City, Map<City, Integer>> graph = CitiesGraphUtils.getGraphFromRoutes(List.of(niceMarseille, marseilleParis, marseilleLyon, lyonParis));

        List<RouteReadOnly> path = CitiesGraphUtils.findShortestPathBetweenCities(graph,allRoutes,nice, paris);
        assertEquals(3, path.size());
        assertEquals(niceMarseille, path.getFirst());
        assertEquals(marseilleLyon, path.get(1));
        assertEquals(lyonParis, path.get(2));
    }

    @Test
    void testFindShortestPath2() {
        City nice = new City("Nice");
        City marseille = new City("Marseille");
        City paris = new City("Paris");
        City lyon = new City("Lyon");

        Route niceMarseille = new Route(nice, marseille, 5, null, null, 0);
        Route marseilleParis = new Route(marseille, paris, 10, null, null, 0);
        Route marseilleLyon = new Route(marseille, lyon, 5, null, null, 0);
        Route lyonParis = new Route(lyon, paris, 5, null, null, 0);


        List<RouteReadOnly> allRoutes = List.of(niceMarseille, marseilleParis, marseilleLyon, lyonParis);
        Map<City, Map<City, Integer>> graph = CitiesGraphUtils.getGraphFromRoutes(List.of(niceMarseille, marseilleParis, marseilleLyon, lyonParis));

        List<RouteReadOnly> path = CitiesGraphUtils.findShortestPathBetweenCities(graph, allRoutes, nice, paris);
        assertEquals(2, path.size());
        assertEquals(niceMarseille, path.getFirst());
        assertEquals(marseilleParis, path.get(1));
    }
}