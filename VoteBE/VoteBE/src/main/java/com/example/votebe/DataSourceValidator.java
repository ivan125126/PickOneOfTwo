package com.example.votebe;

import javax.sql.DataSource;
import java.sql.Connection;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSourceValidator {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void validateConnection() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn != null) {
                System.out.println("Successfully connected to the database.");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            System.out.println("Error during database connection: " + e.getMessage());
        }
    }
}
