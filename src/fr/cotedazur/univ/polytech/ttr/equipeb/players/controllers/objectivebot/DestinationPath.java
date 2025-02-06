package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.objectivebot;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;

import java.util.List;
import java.util.Objects;

public class DestinationPath implements Comparable<DestinationPath> {
    private final DestinationCard destinationCard;
    private final List<RouteReadOnly> routes;
    private final int length;

    public DestinationPath(DestinationCard destinationCard, List<RouteReadOnly> routes, int length) {
        this.destinationCard = destinationCard;
        this.routes = routes;
        this.length = length;
    }

    public DestinationCard getDestinationCard() {
        return destinationCard;
    }

    public int getLength() {
        return length;
    }

    public List<RouteReadOnly> getRoutes() {
        return routes;
    }

    @Override
    public int compareTo(DestinationPath o) {
        return Integer.compare(length, o.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DestinationPath destinationPath) {
            return (destinationPath).length == length;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length);
    }
}