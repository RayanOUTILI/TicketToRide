package fr.cotedazur.univ.polytech.ttr.equipeb.models.score;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.City;

import java.util.Objects;

public class CityPair {
    private final City firstCity;
    private final City secondCity;
    private int minLength;
    private int maxLength;

    public CityPair(City firstCity, City secondCity) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.minLength = 0;
        this.maxLength = 0;
    }

    public City getFirstCity() {
        return firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityPair cityPair = (CityPair) o;
        return (firstCity.equals(cityPair.firstCity) && secondCity.equals(cityPair.secondCity)) ||
                (firstCity.equals(cityPair.secondCity) && secondCity.equals(cityPair.firstCity));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCity, secondCity) + Objects.hash(secondCity, firstCity);
    }

    @Override
    public String toString() {
        return firstCity + " - " + secondCity + " (" + minLength + " - " + maxLength + ")";
    }
}
