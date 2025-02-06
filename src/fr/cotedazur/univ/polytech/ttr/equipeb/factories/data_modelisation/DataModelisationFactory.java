package fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.GameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DataModelisationFactory {

    private List<PlayerModel> playerModels;

    protected DataModelisationFactory(){
        this.playerModels = new ArrayList<>();
    }

    public GameModel initGameDatas(List<PlayerType> players, List<IPlayerViewable> views){
        createPlayerModels(players, views);
        return new GameModel(
                this.playerModels,
                createWagonCardDeck(),
                createshortDestinationCardDeck(),
                createlongDestinationCardDeck(),
                createRoutes()
        );
    }

    /**
     * <p>
     * <b>Important:</b> This method can be called only after the creation of the GameModel.
     * </p>
     * @return PlayerModels
     *
     */
    public Optional<List<PlayerModel>> getPlayerModels() {
        return Optional.ofNullable(playerModels);
    }

    protected void createPlayerModels(List<PlayerType> players, List<IPlayerViewable> views){
        if (players.size() > PlayerIdentification.values().length) {
            throw new IllegalArgumentException("Number of players is greater than the number of player identifications");
        }

        if (players.size() != views.size()) {
            throw new IllegalArgumentException("Number of players and views must be the same");
        }

        for (int i = 0; i < players.size(); i++) {
            playerModels.add(
                    new PlayerModel(
                            PlayerIdentification.values()[i],
                            players.get(i),
                            views.get(i)
                    )
            );
        }
    }

    protected abstract List<Route> createRoutes();
    protected abstract WagonCardDeck createWagonCardDeck();
    protected abstract DestinationCardDeck<DestinationCard> createshortDestinationCardDeck();
    protected abstract DestinationCardDeck<DestinationCard> createlongDestinationCardDeck();
}
