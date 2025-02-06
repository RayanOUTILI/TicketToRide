package fr.cotedazur.univ.polytech.ttr.equipeb.stats;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerIdentification;
import fr.cotedazur.univ.polytech.ttr.equipeb.players.models.PlayerType;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatAction;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.action.StatActionStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsWriterTest {

    private StatsWriter writer;
    private CSVReader csvReader;

    private static final String fileName = "test-resources/writer/";

    private static UUID gameId;

    private static UUID playerIdOne;
    private static UUID playerIdTwo;
    private static UUID playerIdThree;

    private static PlayerIdentification playerColorOne;
    private static PlayerIdentification playerColorTwo;
    private static PlayerIdentification playerColorThree;

    private static PlayerType playerTypeOne;
    private static PlayerType playerTypeTwo;
    private static PlayerType playerTypeThree;

    private static PlayerStatsLine lineOne;
    private static PlayerStatsLine lineTwo;
    private static PlayerStatsLine lineThree;

    @BeforeAll
    static void setUp() {
        gameId = UUID.randomUUID();
        playerIdOne = UUID.randomUUID();
        playerIdTwo = UUID.randomUUID();
        playerIdThree = UUID.randomUUID();
        playerColorOne = PlayerIdentification.RED;
        playerColorTwo = PlayerIdentification.BLUE;
        playerColorThree = PlayerIdentification.GREEN;
        playerTypeOne = PlayerType.EASY_BOT;
        playerTypeTwo = PlayerType.MEDIUM_BOT;
        playerTypeThree = PlayerType.OBJECTIVE_BOT;
        lineOne = createLineOne();
        lineTwo = createLineTwo();
        lineThree = createLineThree();
    }

    @AfterAll
    static void deleteFiles() throws IOException {
        // Here we delete all the files in the resources/tests directory
        File file = new File(fileName);
        if (file.exists()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                f.delete();
            }
        }

        File gitKeep = new File(fileName + ".gitkeep");
        gitKeep.createNewFile();
    }

    private static PlayerStatsLine createLineOne() {
        PlayerStatsLine line = new PlayerStatsLine(playerIdOne, playerColorOne, playerTypeOne, "Label One");
        line.setGameId(gameId);
        line.setAction(StatAction.CLAIM_ROUTE);
        line.setActionStatus(StatActionStatus.YES);
        line.setScore(10);
        line.setWagonsCards(6);
        line.setDestinationCards(3);
        line.setCurrentDestinationScore(15);
        line.setCurrentTime(105);
        return line;
    }

    private static PlayerStatsLine createLineTwo() {
        PlayerStatsLine line = new PlayerStatsLine(playerIdTwo, playerColorTwo, playerTypeTwo, "Label Two");
        line.setGameId(gameId);
        line.setAction(StatAction.PICK_WAGON_CARD);
        line.setActionStatus(StatActionStatus.ACTION_INVALID);
        line.setScore(20);
        line.setWagonsCards(5);
        line.setDestinationCards(2);
        line.setCurrentDestinationScore(10);
        line.setCurrentTime(110);
        return line;
    }

    private static PlayerStatsLine createLineThree() {
        PlayerStatsLine line = new PlayerStatsLine(playerIdThree, playerColorThree, playerTypeThree, "Label Three");
        line.setGameId(gameId);
        line.setAction(StatAction.PICK_DESTINATION_CARDS);
        line.setActionStatus(StatActionStatus.YES);
        line.setScore(30);
        line.setWagonsCards(4);
        line.setDestinationCards(1);
        line.setCurrentDestinationScore(5);
        line.setCurrentTime(115);
        return line;
    }

    private String getFileName(String testName) {
        return fileName + testName + ".csv";
    }

    private void openWriter(String testName) throws IOException {
        writer = new StatsWriter(getFileName(testName), PlayerStatsLine.headers, true);
    }

    private void openCSVReader(String testName) throws IOException {
        csvReader = new CSVReader(new FileReader(getFileName(testName)));
    }

    @Test
    void testClearAfterPush() throws IOException {
        openWriter("clear-after-push");
        writer.commit(lineOne);
        writer.commit(lineTwo);
        writer.commit(lineThree);

        assertEquals(3, writer.buffer.size());

        writer.push();

        assertEquals(0, writer.buffer.size());

        writer.close();
    }

    @Test
    void testWriteStatsWithoutExistingFile() throws IOException, CsvException {
        openWriter("write-stats-without-existing-file");
        writer.commit(lineOne);
        writer.commit(lineTwo);
        writer.commit(lineThree);

        writer.push();
        writer.close();

        openCSVReader("write-stats-without-existing-file");
        List<String[]> lines = csvReader.readAll();
        csvReader.close();

        // Check the number of lines
        assertEquals(4, lines.size());

        // Check the header
        assertArrayEquals(PlayerStatsLine.headers, lines.getFirst());

        // Check the lines
        assertArrayEquals(lineOne.getValues(), lines.get(1));
        assertArrayEquals(lineTwo.getValues(), lines.get(2));
        assertArrayEquals(lineThree.getValues(), lines.get(3));
    }

    @Test
    void testWriteStatsWithAppend() throws IOException, CsvException {
        openWriter("write-stats-with-append");
        writer.commit(lineOne);
        writer.push();
        writer.close();

        openWriter("write-stats-with-append");
        writer.commit(lineTwo);
        writer.push();
        writer.close();

        openWriter("write-stats-with-append");
        writer.commit(lineThree);
        writer.push();
        writer.close();

        openCSVReader("write-stats-with-append");
        List<String[]> lines = csvReader.readAll();
        csvReader.close();

        // Check the number of lines
        assertEquals(4, lines.size());

        // Check the header
        assertArrayEquals(PlayerStatsLine.headers, lines.getFirst());

        // Check the lines
        assertArrayEquals(lineOne.getValues(), lines.get(1));
        assertArrayEquals(lineTwo.getValues(), lines.get(2));
        assertArrayEquals(lineThree.getValues(), lines.get(3));
    }
}