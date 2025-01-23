package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.JsonParseException;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteParser extends BaseParser<List<Map<String, Object>>> {

    /**
     * Parses the routes from the given JSON file.
     *
     * @param filePath The path to the JSON file.
     * @return The list of routes.
     */
    public List<Route> parseRoutes(String filePath) throws JsonParseException {
        List<Route> routes = new ArrayList<>();
        List<Map<String, Object>> routeDataList = parseJsonFile(filePath, new TypeReference<>() {});

        for (Map<String, Object> routeData : routeDataList) {
            String start = (String) routeData.get("start");
            String end = (String) routeData.get("end");
            int size = (Integer) routeData.get("size");
            String type = (String) routeData.get("type");
            String color = (String) routeData.get("color");
            int nbLocomotives = (Integer) routeData.get("nbLocomotives");

            City city1 = getOrCreateCity(start);
            City city2 = getOrCreateCity(end);
            RouteType routeType = RouteType.valueOf(type.toUpperCase());
            Color routeColor = Color.valueOf(color.toUpperCase());

            routes.add(new Route(city1, city2, size, routeType, routeColor, nbLocomotives));
        }

        return routes;
    }
}
