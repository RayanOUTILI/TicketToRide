package fr.cotedazur.univ.polytech.ttr.equipeb.utils;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CitiesGraphUtilsTest {

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
}