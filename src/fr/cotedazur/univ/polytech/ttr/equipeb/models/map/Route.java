package fr.cotedazur.univ.polytech.ttr.equipeb.models.map;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

/**
 * Represents a route between two cities on the map
 */
public class Route implements NonControllableRoute {

    private final City firstCity;

    private final City secondCity;

    private final int length;

    private PlayerIdentification claimerPlayer;

    public Route(City firstCity, City secondCity, int length) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = length;
        claimerPlayer = null;
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

}