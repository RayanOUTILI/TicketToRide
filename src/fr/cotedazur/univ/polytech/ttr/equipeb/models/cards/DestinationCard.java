package fr.cotedazur.univ.polytech.ttr.equipeb.models.cards;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

public class DestinationCard {
    private final City startCity;
    private final City endCity;
    private final int points;

    public DestinationCard(City startCity, City endCity, int points) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.points = points;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public int getPoints() {
        return points;
    }

    public List<City> getCities() {
        return List.of(startCity, endCity);
    }

}
