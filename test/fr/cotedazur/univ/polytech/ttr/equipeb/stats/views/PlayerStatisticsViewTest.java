package fr.cotedazur.univ.polytech.ttr.equipeb.stats.views;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.Action;
import fr.cotedazur.univ.polytech.ttr.equipeb.actions.ReasonActionRefused;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.cards.DestinationCard;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.game.IStatsGameModel;
import fr.cotedazur.univ.polytech.ttr.equipeb.models.map.RouteReadOnly;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.IPlayerModelStats;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.StatsWriter;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatAction;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatActionStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

class PlayerStatisticsViewTest {

    private IPlayerModelStats playerModel;
    private IStatsGameModel gameModel;

    private static int runnedTest = 0;
    private static final String PATH = "test-resources/view/";
    private PlayerStatisticsView playerStatisticsView;
    private StatsWriter statsWriter;

    private CSVReader csvReader;

    private UUID gameId;
    private UUID playerId;
    private PlayerType playerType;
    private PlayerIdentification playerColor;
    private int currentTurn;
    private StatAction action;
    private StatActionStatus actionStatus;
    private int score;
    private int wagonsCards;
    private List<DestinationCard> destinationCardsList;
    private List<RouteReadOnly> routes;
    private String label;

    private String getTestPath() {
        return PATH + "test-" + runnedTest + ".csv";
    }

    private List<String[]> readCSV() throws IOException, CsvException {
        csvReader = new CSVReader(new FileReader(getTestPath()));
        List<String[]> lines = csvReader.readAll();
        csvReader.close();
        return lines;
    }

    private String[] removeSystemMilis(String[] line){
        String[] newLine = new String[line.length];
        for (int i = 0; i < line.length; i++) {
            if (i == 0) {
                newLine[i] = "0";
            } else {
                newLine[i] = line[i];
            }
        }
        return newLine;
    }

    private String[] getExpectedValues(){
        return new String[]{
                "0",
                gameId.toString(),
                playerId.toString(),
                playerType.toString(),
                playerColor.getLabel(),
                String.valueOf(currentTurn),
                action.toString(),
                actionStatus.toString(),
                String.valueOf(score),
                String.valueOf(wagonsCards),
                destinationCardsList.size() + "",
                routes.size() + "",
                label
        };
    }

    @BeforeEach
    void setUp() throws IOException {
        this.playerId = UUID.randomUUID();
        this.gameId = UUID.randomUUID();
        this.playerType = PlayerType.MEDIUM_BOT;
        this.playerColor = PlayerIdentification.BLACK;
        this.currentTurn = 1;
        this.score = 10;
        this.wagonsCards = 63;
        this.destinationCardsList = List.of();
        this.routes = List.of();
        this.label = "Test Game";

        PlayerStatsLine statsLine = new PlayerStatsLine(playerId, playerColor, playerType, label);
        this.playerModel = Mockito.mock(IPlayerModelStats.class);
        this.gameModel = Mockito.mock(IStatsGameModel.class);
        this.statsWriter = new StatsWriter(getTestPath(), PlayerStatsLine.headers, false);
        this.playerStatisticsView = new PlayerStatisticsView(statsLine, statsWriter);
        this.playerStatisticsView.setGameModel(gameModel);

        when(gameModel.getPlayerWithIdentification(playerColor)).thenReturn(playerModel);
        when(playerModel.getPlayerType()).thenReturn(PlayerType.MEDIUM_BOT);
        when(playerModel.getScore()).thenReturn(score);
        when(playerModel.getNumberOfWagonCards()).thenReturn(wagonsCards);
        when(playerModel.getDestinationCards()).thenReturn(destinationCardsList);
        when(playerModel.getSelectedStationRoutes()).thenReturn(routes);
        when(gameModel.getAllRoutesClaimedByPlayer(playerColor)).thenReturn(routes);
    }

    @AfterEach
    void tearDown() {
        File file = new File(getTestPath());
        file.delete();
        runnedTest++;
    }

    @Test
    void testWriteFromActionCompleted() throws IOException, CsvException {
        this.action = StatAction.CLAIM_ROUTE;
        this.actionStatus = StatActionStatus.YES;

        playerStatisticsView.displayNewGame(gameId);
        playerStatisticsView.displayNewTurn(currentTurn);
        playerStatisticsView.displayActionCompleted(Action.valueOf(action.toString()));
        playerStatisticsView.writeStats();

        statsWriter.close();

        assertArrayEquals(removeSystemMilis(readCSV().get(1)), getExpectedValues());
    }

    @Test
    void testWriteFromActionRefused() throws IOException, CsvException {
        this.action = StatAction.PICK_DESTINATION_CARDS;
        this.actionStatus = StatActionStatus.ACTION_INVALID;


        playerStatisticsView.displayNewGame(gameId);
        playerStatisticsView.displayNewTurn(currentTurn);
        playerStatisticsView.displayActionRefused(Action.valueOf(action.toString()), ReasonActionRefused.ACTION_INVALID);
        playerStatisticsView.writeStats();

        statsWriter.close();

        assertArrayEquals(removeSystemMilis(readCSV().get(1)), getExpectedValues());
    }

    @Test
    void testWriteFromActionStop() throws IOException, CsvException {
        this.action = StatAction.STOP;
        this.actionStatus = StatActionStatus.YES;

        playerStatisticsView.displayNewGame(gameId);
        playerStatisticsView.displayNewTurn(currentTurn);
        playerStatisticsView.displayActionStop();
        playerStatisticsView.writeStats();

        statsWriter.close();

        assertArrayEquals(removeSystemMilis(readCSV().get(1)), getExpectedValues());
    }

    @Test
    void testWriteWinner() throws IOException, CsvException {
        this.action = StatAction.WINNER;
        this.actionStatus = StatActionStatus.YES;

        playerStatisticsView.displayNewGame(gameId);
        playerStatisticsView.displayNewTurn(currentTurn);
        playerStatisticsView.displayWinner(playerColor, score);
        playerStatisticsView.writeStats();

        statsWriter.close();

        assertArrayEquals(removeSystemMilis(readCSV().get(1)), getExpectedValues());
    }

    @Test
    void multipleWriteAction() throws IOException, CsvException {
        this.action = StatAction.CLAIM_ROUTE;
        this.actionStatus = StatActionStatus.YES;

        playerStatisticsView.displayNewGame(gameId);
        playerStatisticsView.displayNewTurn(currentTurn);
        playerStatisticsView.displayActionCompleted(Action.valueOf(action.toString()));
        playerStatisticsView.displayActionCompleted(Action.valueOf(action.toString()));
        playerStatisticsView.displayActionCompleted(Action.valueOf(action.toString()));
        playerStatisticsView.writeStats();

        statsWriter.close();

        List<String[]> lines = readCSV();
        for (int i = 1; i < lines.size(); i++) {
            assertArrayEquals(removeSystemMilis(lines.get(i)), getExpectedValues());
        }
    }

}