package fr.cotedazur.univ.polytech.ttr.equipeb.models.game;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;

import java.util.List;

public interface IScoreControllerGameModel {
    List<RouteReadOnly> getAllRoutesClaimedByPlayer(PlayerIdentification player);

    List<PlayerIdentification> getPlayersIdentification();
}
