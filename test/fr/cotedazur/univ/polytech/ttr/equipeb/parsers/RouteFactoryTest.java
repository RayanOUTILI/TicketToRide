package fr.cotedazur.univ.polytech.ttr.equipeb.parsers;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.RouteParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapFactoryTest {

    private MapFactory mapFactory;
    private RouteParser routeParserMock;

    /**
     * Initializes the MapFactory and a mocked RouteParser before each test.
     */
    @BeforeEach
    void setUp() {
        routeParserMock = mock(RouteParser.class);
        mapFactory = new MapFactory(routeParserMock);
    }

    /**
     * Test to ensure that valid JSON files are correctly parsed to create a list of routes.
     */
    @Test
    void testGetMapFromJson() throws JsonParseException {
        List<Route> mockRoutes = List.of(new Route(new City("Paris"), new City("Lyon"), 4, RouteType.TRAIN, Color.RED, 0));
        when(routeParserMock.parseRoutes(anyString())).thenReturn(mockRoutes);

        List<Route> routes = mapFactory.getMapFromJson();

        assertNotNull(routes, "The list of routes should not be null.");
        assertFalse(routes.isEmpty(), "The list of routes should not be empty.");
        assertEquals(1, routes.size(), "The list should contain one route.");

        Route route = routes.get(0);
        assertEquals("Paris", route.getFirstCity().getName());
        assertEquals("Lyon", route.getSecondCity().getName());
        assertEquals(4, route.getLength());
        assertEquals(RouteType.TRAIN, route.getType());
        assertEquals(Color.RED, route.getColor());
    }

    /**
     * Test the overloaded method getMapFromJson with a custom file path.
     */
    @Test
    void testGetMapFromJsonWithFilePath() throws JsonParseException {
        List<Route> mockRoutes = List.of(new Route(new City("Nice"), new City("Marseille"), 5, RouteType.TRAIN, Color.YELLOW, 0));
        when(routeParserMock.parseRoutes("custom-path/routes.json")).thenReturn(mockRoutes);

        List<Route> routes = mapFactory.getMapFromJson("custom-path/routes.json");

        assertNotNull(routes, "The list of routes should not be null.");
        assertFalse(routes.isEmpty(), "The list of routes should not be empty.");
        assertEquals(1, routes.size(), "The list should contain one route.");
        assertEquals("Nice", routes.get(0).getFirstCity().getName());
        assertEquals("Marseille", routes.get(0).getSecondCity().getName());
    }

    /**
     * Test to ensure that the parser throws a JsonParseException when given an invalid JSON file.
     */
    @Test
    void testGetMapFromJsonWithInvalidFile() throws JsonParseException {
        when(routeParserMock.parseRoutes(anyString())).thenThrow(new JsonParseException("Invalid file"));

        assertThrows(JsonParseException.class, () -> mapFactory.getMapFromJson(), "A JsonParseException should be thrown.");
    }

    /**
     * Test to ensure that getSmallMap returns a small, predefined map with a known route.
     */
    @Test
    void testGetSmallMap() {
        List<Route> smallMap = mapFactory.getSmallMap();

        assertNotNull(smallMap, "The small map should not be null.");
        assertFalse(smallMap.isEmpty(), "The small map should not be empty.");
        assertEquals(1, smallMap.size(), "The small map should contain exactly one route.");

        Route route = smallMap.get(0);
        assertEquals("Paris", route.getFirstCity().getName(), "The first city should be Paris.");
        assertEquals("Brest", route.getSecondCity().getName(), "The second city should be Brest.");
        assertEquals(3, route.getLength(), "The route length should be 3.");
        assertEquals(RouteType.TRAIN, route.getType(), "The route type should be TRAIN.");
        assertEquals(Color.BLUE, route.getColor(), "The route color should be BLUE.");
    }
}
