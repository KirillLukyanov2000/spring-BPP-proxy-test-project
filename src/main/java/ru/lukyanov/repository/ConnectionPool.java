package ru.lukyanov.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionPool {

    private final HikariDataSource ds;


    public ConnectionPool() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/game");
        config.setUsername("root");
        config.setPassword("root");

        ds = new HikariDataSource(config);
    }




    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
