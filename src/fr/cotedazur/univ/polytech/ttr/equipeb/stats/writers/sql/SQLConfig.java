package fr.cotedazur.univ.polytech.ttr.equipeb.stats.writers.sql;

import io.github.cdimascio.dotenv.Dotenv;

public class SQLConfig {
    private final String url;
    private final String user;
    private final String password;

    public SQLConfig() {
        Dotenv dotenv = Dotenv.load();
        this.url = dotenv.get("DB_URL");
        this.user = dotenv.get("DB_USER");
        this.password = dotenv.get("DB_PASSWORD");

    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
