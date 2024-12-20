package fr.cotedazur.univ.polytech.ttr.equipeb.models.endgame;

import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

/**
 * Victory class
 */
public record Victory(PlayerIdentification player, EndGameReasons reason) {
    public PlayerIdentification getPlayerIdentification() {
        return player;
    }
}
