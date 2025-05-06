package com.hei.fifa_gestion_central.database;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class Datasource {
    private final String host = System.getenv("DATABASE_HOST");
    private final String user = System.getenv("DATABASE_USER");
    private final String password = System.getenv("DATABASE_PASSWORD");
    private final int defaultPort = 5432;
    private final String database = System.getenv("DATABASE_NAME");
    private final String jdbcUrl;

    public Datasource() {
        jdbcUrl = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + database;
    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(jdbcUrl, user, password);
        }
        catch (SQLException e){
            System.err.println("Erreur de connexion à la base de données eee : " + e.getMessage());
            throw new RuntimeException("Impossible d'obtenir une connexion à la base de données.", e);
        }
    }
}
