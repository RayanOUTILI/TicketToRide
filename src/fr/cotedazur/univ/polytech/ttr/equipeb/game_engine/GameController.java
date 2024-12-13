package fr.cotedazur.univ.polytech.ttr.equipeb.game_engine;

public class GameController {

    private RouteController routeController;
    private DeckController deckController;
    private ScoreController scoreCalculator;

    public GameController(RouteController routeController, DeckController deckController, ScoreController scoreCalculator) {
        this.routeController = routeController;
        this.deckController = deckController;
        this.scoreCalculator = scoreCalculator;
    }

}
