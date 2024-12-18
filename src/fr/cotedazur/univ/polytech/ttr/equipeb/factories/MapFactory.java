package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers.RouteParser;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteColor;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFactory {
    private final RouteParser routeParser;

    public MapFactory() {
        this.routeParser = new RouteParser();
    }

    public List<Route> getMapFromJson(String filePath){
        return routeParser.parseRoutes(filePath);
    }

    public List<Route> getSmallMap() {
        List<Route> routes = new ArrayList<>();
        City paris = new City("Paris");
        City brest = new City("Brest");
        routes.add(new Route(paris, brest, 3, RouteType.TRAIN, RouteColor.BLUE, 0));
        return routes;
    }
}
