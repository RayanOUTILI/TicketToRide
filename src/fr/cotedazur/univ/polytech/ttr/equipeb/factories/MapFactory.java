package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;

import java.util.ArrayList;
import java.util.List;

public class MapFactory {
    public List<Route> getSmallMap() {
        List<Route> routes = new ArrayList<>();
        City paris = new City("Paris");
        City brest = new City("Brest");
        routes.add(new Route(paris, brest, 3));
        return routes;
    }
}
