package fr.cotedazur.univ.polytech.ttr.equipeb.map;

import java.util.ArrayList;
import java.util.List;

public class City {
    private String name;
    private List<Route> routes;

    public City(String name) {
        this.name = name;
        this.routes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }
}
