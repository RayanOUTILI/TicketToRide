package fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.randombots;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IPlayerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.controllers.BotEngineControllable;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerEngineViewable;
import fr.cotedazur.univ.polytech.ttr.equipeb.utils.RandomGenerator;

public abstract class BotEngineWithRandom  extends BotEngineControllable {
    protected final RandomGenerator random;

    protected BotEngineWithRandom( IPlayerGameModel gameModel, IPlayerModel playerModel, IPlayerEngineViewable view) {
        super(gameModel, playerModel, view);
        this.random = new RandomGenerator();
    }
}
