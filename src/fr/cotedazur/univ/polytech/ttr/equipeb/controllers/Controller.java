package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.Optional;

public abstract class Controller {
    public abstract boolean initGame();
    public abstract boolean initPlayer(Player player);
    public abstract Optional<ReasonActionRefused> doAction(Player player);
}
