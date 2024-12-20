package fr.cotedazur.univ.polytech.ttr.equipeb.players.models;

/**
 * Enumeration to identify the players
 */
public enum PlayerIdentification {
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    RED("\u001B[31m"),
    BLACK("\u001B[30m");

    private final String code;

    PlayerIdentification(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code + super.toString() + "\u001B[0m";
    }
}
