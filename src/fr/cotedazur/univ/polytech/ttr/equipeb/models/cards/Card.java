package fr.cotedazur.univ.polytech.ttr.equipeb.models.cards;

public class Card {
    public void showCard() {
    }

    @Override
    public String toString() {
        return "This is a card" + this.getClass().getSimpleName();
    }
}
