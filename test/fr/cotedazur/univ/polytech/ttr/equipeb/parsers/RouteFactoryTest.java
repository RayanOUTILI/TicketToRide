package fr.cotedazur.univ.polytech.ttr.equipeb.parsers;

import fr.cotedazur.univ.polytech.ttr.equipeb.factories.MapFactory;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.RouteParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RouteFactoryTest {

    private MapFactory mapFactory;
    private RouteParser routeParserMock;

    /**
     * Initializes the MapFactory and a mocked RouteParser before each test.
     */
    @BeforeEach
    void setUp() {
        routeParserMock = mock(RouteParser.class); // Create a mock for RouteParser
        mapFactory = new MapFactory(routeParserMock); // Inject the mock into MapFactory
    }

    /**
     * Test to ensure that valid JSON files are correctly parsed to create a list of routes.
     * Verifies that the list of routes is not null, not empty, and checks specific attributes of a route.
     */
    @Test
    void testParseRoutesFromJson() {
        try {
            MapFactory mapFactory = new MapFactory();
            List<Route> routes = mapFactory.getMapFromJson();

            assertNotNull(routes, "The list of routes should not be null.");
            assertFalse(routes.isEmpty(), "The list of routes should not be empty.");

            assertEquals(101, routes.size(), "The list of routes should contain exactly 101 routes.");

            Route route = routes.get(0);
            assertNotNull(route.getFirstCity(), "The first city of the route should not be null.");
            assertNotNull(route.getSecondCity(), "The second city of the route should not be null.");
            assertTrue(route.getLength() > 0, "The length of the route should be greater than zero.");
            assertNotNull(route.getColor(), "The color of the route should not be null.");
            assertNotNull(route.getType(), "The type of the route should not be null.");
        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }

    /**
     * Test to ensure that the parser throws a JsonParseException when provided with an invalid JSON file.
     */
    @Test
    void testParseRoutesFromJsonWithInvalidFile() {
        try {
            when(routeParserMock.parseRoutes(anyString())).thenThrow(new JsonParseException("Invalid file"));

            assertThrows(JsonParseException.class, () -> mapFactory.getMapFromJson(), "A JsonParseException should be thrown.");
        } catch (JsonParseException e) {
            fail("A parsing exception was thrown: " + e.getMessage());
        }
    }
}
