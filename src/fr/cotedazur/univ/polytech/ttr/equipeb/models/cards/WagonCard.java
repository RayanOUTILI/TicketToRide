package fr.cotedazur.univ.polytech.ttr.equipeb.models.cards;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.colors.Color;

public class WagonCard extends Card {
    private final Color color;
    public WagonCard(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "This is a " + this.getClass().getSimpleName() + " of color " + color;
    }
}
