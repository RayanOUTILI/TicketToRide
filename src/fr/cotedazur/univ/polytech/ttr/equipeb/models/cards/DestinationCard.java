package fr.cotedazur.univ.polytech.ttr.equipeb.models.cards;

public class DestinationCard extends Card{
    private final String startCity;
    private final String endCity;
    private final int points;

    public DestinationCard(String startCity, String endCity, int points) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.points = points;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public int getPoints() {
        return points;
    }

}
