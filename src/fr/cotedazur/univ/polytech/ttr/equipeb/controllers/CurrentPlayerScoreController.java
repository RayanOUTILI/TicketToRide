package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.ICurrentPlayerScoreControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;

import java.util.Optional;

public class CurrentPlayerScoreController extends Controller {
    private final ICurrentPlayerScoreControllerGameModel gameModel;

    public CurrentPlayerScoreController(ICurrentPlayerScoreControllerGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public boolean initGame() {
        return true;
    }

    @Override
    public boolean initPlayer(Player player) {
        return true;
    }

    @Override
    public Optional<ReasonActionRefused> doAction(Player player) {
        player.setScore(gameModel.getAllRoutesClaimedByPlayer(player.getIdentification())
                .stream()
                .mapToInt(route -> switch (route.getLength()) {
                    case 1 -> 1;
                    case 2 -> 2;
                    case 3 -> 4;
                    case 4 -> 7;
                    case 6 -> 15;
                    case 8 -> 21;
                    default -> 0;
                })
                .sum());
        return Optional.empty();
    }

    @Override
    public boolean resetPlayer(Player player) {
        return true;
    }

    @Override
    public boolean resetGame() {
        return true;
    }
}
