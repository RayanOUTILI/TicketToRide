package fr.cotedazur.univ.polytech.ttr.equipeb.factories.data_modelisation;

import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.DestinationCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.deck.WagonCardDeck;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.Route;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.views.IPlayerViewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

class DataModelisationFactoryTest {

    @Mock
    private List<Route> routes;
    @Mock
    private WagonCardDeck wagonCardDeck;
    @Mock
    private DestinationCardDeck<DestinationCard> shortDestinationCardDeck;
    @Mock
    private DestinationCardDeck<DestinationCard> longDestinationCardDeck;
    @Mock
    private List<IPlayerViewable> playerViews;

    private DataModelisationFactory dataModelisationFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataModelisationFactory = mock(DataModelisationFactory.class, CALLS_REAL_METHODS);
    }

    @Test
    void getPlayerModelsReturnsEmptyOptionalIfNotInitialized() {
        Optional<List<PlayerModel>> playerModels = dataModelisationFactory.getPlayerModels();
        assertTrue(playerModels.isEmpty());
    }

    @Test
    void createPlayerModelsThrowsExceptionIfPlayerCountExceedsIdentifications() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT, PlayerType.MEDIUM_BOT, PlayerType.MEDIUM_BOT, PlayerType.OBJECTIVE_BOT);
        assertThrows(IllegalArgumentException.class, () -> dataModelisationFactory.createPlayerModels(playerTypes, playerViews));
    }

    @Test
    void createPlayerModelsThrowsExceptionIfPlayerCountDoesNotMatchViewCount() {
        List<PlayerType> playerTypes = List.of(PlayerType.EASY_BOT, PlayerType.MEDIUM_BOT);
        List<IPlayerViewable> mismatchedViews = List.of(mock(IPlayerViewable.class));
        assertThrows(IllegalArgumentException.class, () -> dataModelisationFactory.createPlayerModels(playerTypes, mismatchedViews));
    }

}