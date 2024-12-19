package fr.cotedazur.univ.polytech.ttr.equipeb.models.map;

public class City {
    private final String name;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof City city)) {
            return false;
        }
        return getName().equals(city.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
