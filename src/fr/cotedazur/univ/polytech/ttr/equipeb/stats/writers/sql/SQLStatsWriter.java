package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.sql;

import fr.cotedazur.univ.polytech.ttr.equipeb.stats.PlayerStatsLine;
import fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.StatsWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLStatsWriter extends StatsWriter {
    private final Connection conn;

    public SQLStatsWriter(boolean truncate) throws SQLException {
        super();
        SQLConfig config = new SQLConfig();
        DriverManager.setLoginTimeout(5);
        this.conn = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        if (truncate) {
            truncate();
        }
    }

    private void truncate() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("TRUNCATE TABLE gamestats");
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void push() {
        if (getReadableBuffer().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO gamestats (timestamp, game_id, player_id, player_type, player_color, current_turn, action, action_status, action_skipped, displayed_score, wagons_cards_hand_count, destination_cards_hand_count, calculated_current_destination_score) VALUES ";
        for (PlayerStatsLine line : getReadableBuffer()) {
            sql += "(" + line.getCurrentTime() + ",";
            sql += "'" + line.getGameId() + "',";
            sql += "'" + line.getPlayerId() + "',";
            sql += "'" + line.getPlayerType() + "',";
            sql += "'" + line.getPlayerColor().getLabel() + "',";
            sql += line.getCurrentTurn() + ",";
            sql += "'" + line.getAction() + "',";
            sql += "'" + line.getActionStatus() + "',";
            sql += "'" + (line.isActionSkip() ? "YES" : "NO") + "',";
            sql += line.getScore() + ",";
            sql += line.getWagonsCards() + ",";
            sql += line.getDestinationCards() + ",";
            sql += line.getCurrentDestinationScore() + "),";
        }

        sql = sql.substring(0, sql.length() - 1);

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clearBuffer();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
