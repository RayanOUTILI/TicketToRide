package fr.cotedazur.univ.polytech.ttr.equipeb.models.score;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.util.Objects;

public class CityPair {
    private final City firstCity;
    private final City secondCity;
    private int length;

    public CityPair(City firstCity, City secondCity) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = 0;
    }

    public CityPair(City firstCity, City secondCity, int length) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = length;
    }

    public City getFirstCity() {
        return firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityPair cityPair = (CityPair) o;
        return Objects.equals(firstCity, cityPair.firstCity) && Objects.equals(secondCity, cityPair.secondCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCity, secondCity);
    }

    @Override
    public String toString() {
        return firstCity + " - " + secondCity + " (" + length + ")";
    }
}
