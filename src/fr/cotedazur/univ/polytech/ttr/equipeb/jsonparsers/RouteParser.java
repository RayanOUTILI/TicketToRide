package fr.cotedazur.univ.polytech.ttr.equipeb.jsonparsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteColor;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteParser {
    private ObjectMapper objectMapper;
    private Map<String, City> cityMap;

    public RouteParser() {
        this.objectMapper = new ObjectMapper();
        this.cityMap = new HashMap<>();
    }

    public List<Route> parseRoutes(String filePath){
        List<Route> routes = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            List<Map<String, Object>> routeDataList = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>(){});


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
                RouteColor routeColor = RouteColor.valueOf(color.toUpperCase());

                routes.add(new Route(city1, city2, size, routeType, routeColor, nbLocomotives));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routes;
    }

    /**
     * Get the city with the given name or create it if it does not exist
     * @param cityName the name of the city
     * @return the city
     */
    private City getOrCreateCity(String cityName) {
        return cityMap.computeIfAbsent(cityName, City::new);
    }
}