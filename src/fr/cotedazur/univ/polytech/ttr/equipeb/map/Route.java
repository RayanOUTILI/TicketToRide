package fr.cotedazur.univ.polytech.ttr.equipeb.map;

import fr.cotedazur.univ.polytech.ttr.equipeb.exceptions.ExceedMaxWagonsException;

public class Route {
    private City city1;
    private City city2;
    private int capacity;
    private int placedWagons;

    public Route(City city1, City city2, int length) {
        this.city1 = city1;
        this.city2 = city2;
        this.capacity = length;
        this.placedWagons = 0;
        city1.addRoute(this);
        city2.addRoute(this);
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPlacedWagons() {
        return placedWagons;
    }

    public void addWagon() {
        if (placedWagons < capacity) {
            placedWagons++;
        } else {
            throw new ExceedMaxWagonsException("The number of placed wagons exceeds the length of the route.");
        }
    }
}