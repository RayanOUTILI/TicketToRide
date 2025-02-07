package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.sql;

import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.StatsWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLStatsWriter extends StatsWriter {
    private final Connection conn;
    private final Logger logger = LoggerFactory.getLogger(SQLStatsWriter.class);

    public SQLStatsWriter(boolean truncate) throws SQLException {
        super();
        SQLConfig config = new SQLConfig();
        DriverManager.setLoginTimeout(5);
        this.conn = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        if (truncate) {
            truncate();
        }
    }

    private void truncate() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE gamestats");
        }
    }

    @Override
    public void push() {
        if (getReadableBuffer().isEmpty()) {
            return;
        }
        StringBuilder query = new StringBuilder("INSERT INTO gamestats (timestamp, game_id, player_id, player_type, player_color, current_turn, action, action_status, action_skipped, displayed_score, wagons_cards_hand_count, destination_cards_hand_count, calculated_current_destination_score) VALUES ");

        for (PlayerStatsLine line : getReadableBuffer()) {
            query.append("(")
                    .append(line.getCurrentTime()).append(", ")
                    .append("'").append(line.getGameId()).append("', ")
                    .append("'").append(line.getPlayerId()).append("', ")
                    .append("'").append(line.getPlayerType()).append("', ")
                    .append("'").append(line.getPlayerColor().getLabel()).append("', ")
                    .append(line.getCurrentTurn()).append(", ")
                    .append("'").append(line.getAction()).append("', ")
                    .append("'").append(line.getActionStatus()).append("', ")
                    .append("'").append(line.isActionSkip() ? "YES" : "NO").append("', ")
                    .append(line.getScore()).append(", ")
                    .append(line.getWagonsCards()).append(", ")
                    .append(line.getDestinationCards()).append(", ")
                    .append(line.getCurrentDestinationScore()).append("), ");
        }

        query.deleteCharAt(query.length() - 2);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query.toString());
        } catch (Exception e) {
            logger.error("Error while pushing stats to SQL", e);
        }

        this.clearBuffer();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
