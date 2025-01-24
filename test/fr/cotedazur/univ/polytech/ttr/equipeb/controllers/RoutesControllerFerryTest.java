package fr.cotedazur.univ.polytech.ttr.equipeb.controllers;

import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ClaimRoute;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.WagonCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IRoutesControllerGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.mockito.Mockito.mock;

public class RoutesControllerFerryTest {
    private RoutesController routesController;
    private IRoutesControllerGameModel gameModel;
    private Player player;
    private ClaimRoute claimRoute;
    private Route route;
    private List<WagonCard> wagonCards;

    @BeforeEach
    void setup() {
        gameModel = mock(IRoutesControllerGameModel.class);

    }
}
