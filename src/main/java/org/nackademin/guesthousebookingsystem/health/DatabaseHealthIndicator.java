package org.nackademin.guesthousebookingsystem.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(3)) {
                return Health.up()
                        .withDetail("database", "Databasen är aktiv och anslutningen fungerar")
                        .withDetail("databaseProduct", connection.getMetaData().getDatabaseProductName())
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "Databasen svarar inte inom tidsgränsen")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "Kunde inte ansluta till databasen")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}