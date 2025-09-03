package com.example.SpringBootTask.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@AllArgsConstructor
public class CustomDatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    @Override
    public Health health() {
        try (var connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail("database", "OK").build();
            } else {
                return Health.down().withDetail("database", "Not valid").build();
            }
        } catch (SQLException e) {
            return Health.down(e).build();
        }
    }
}
