package fr.cotedazur.univ.polytech.ttr.equipeb.factories;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.EasyBotEngine;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;

public class PlayerFactory {
    public Player createEasyBot(PlayerModel playerModel, GameModel gameModel) {
        return new Player(new EasyBotEngine(playerModel, gameModel), playerModel);
    }
}
