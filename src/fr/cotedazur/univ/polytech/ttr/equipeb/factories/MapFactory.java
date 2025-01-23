package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.RouteParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;

import java.util.ArrayList;
import java.util.List;

public class MapFactory {
    private final RouteParser routeParser;

    /**
     * Default constructor initializing with a standard RouteParser.
     */
    public MapFactory() {
        this.routeParser = new RouteParser();
    }

    /**
     * Constructor allowing dependency injection of a custom RouteParser.
     * This is primarily used for testing purposes.
     *
     * @param routeParser the RouteParser to use
     */
    public MapFactory(RouteParser routeParser) {
        this.routeParser = routeParser;
    }

    /*
     * Get the map from the JSON file
     */
    public List<Route> getMapFromJson() throws JsonParseException {
        return routeParser.parseRoutes("data-europe/routes.json");
    }

    /**
     * Get the map from the JSON file
     * Test purpose
     * @param filePath the path to the JSON file
     * @return the list of routes
     */
    protected List<Route> getMapFromJson(String filePath) throws JsonParseException {
        return routeParser.parseRoutes(filePath);
    }

    public List<Route> getSmallMap() {
        List<Route> routes = new ArrayList<>();
        City paris = new City("Paris");
        City brest = new City("Brest");
        routes.add(new Route(paris, brest, 3, RouteType.TRAIN, Color.BLUE, 0));
        return routes;
    }
}
