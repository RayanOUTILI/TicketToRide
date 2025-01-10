package fr.cotedazur.univ.polytech.ttr.equipeb.models.map;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.Objects;

public class City {
    private final String name;
    private PlayerIdentification owner = null;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasOwner() {
        return owner == null;
    }

    public void setOwner(PlayerIdentification owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
