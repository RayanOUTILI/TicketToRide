package fr.cotedazur.univ.polytech.ttr.equipeb.models.cards;

public class WagonCard extends Card {
    private final CardColor color;
    public WagonCard(CardColor color) {
        this.color = color;
    }

    public CardColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "This is a " + this.getClass().getSimpleName() + " of color " + color;
    }
}
