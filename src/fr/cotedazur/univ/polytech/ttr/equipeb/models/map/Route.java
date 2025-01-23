package fr.cotedazur.univ.polytech.ttr.equipeb.models.map;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

/**
 * Represents a route between two cities on the map
 */
public class Route implements RouteReadOnly {
    private static int idCounter = 0;

    private final int id;

    private final City firstCity;

    private final City secondCity;

    private final int length;

    private final RouteType type;

    private final Color color;

    private PlayerIdentification claimerPlayer;

    private final int nbLocomotives;

    public Route(City firstCity, City secondCity, int length, RouteType type, Color color, int nbLocomotives) {
        id = idCounter++;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = length;
        this.type = type;
        this.color = color;
        claimerPlayer = null;
        this.nbLocomotives = nbLocomotives;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public City getFirstCity() {
        return firstCity;
    }

    @Override
    public City getSecondCity() {
        return secondCity;
    }

    @Override
    public int getLength() {
        return length;
    }

    public RouteType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public int getNbLocomotives() {
        return nbLocomotives;
    }

    @Override
    public boolean isClaimed() {
        return claimerPlayer != null;
    }

    @Override
    public PlayerIdentification getClaimerPlayer() {
        return claimerPlayer;
    }

    /**
     * Set the player who claimed the route
     * @param player the player who claimed the route
     */
    public void setClaimerPlayer(PlayerIdentification player) {
        claimerPlayer = player;
    }

    @Override
    public String toString() {
        return String.format("Route [%d] from '%s' to '%s': length=%d, color=%s, locomotives=%d, type=%s, %s",
                id,
                firstCity.getName(),
                secondCity.getName(),
                length,
                color,
                nbLocomotives,
                type,
                isClaimed() ? "claimed by " + claimerPlayer : "not claimed"
        );
    }


}