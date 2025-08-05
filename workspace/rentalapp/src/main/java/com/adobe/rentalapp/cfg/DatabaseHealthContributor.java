package com.adobe.rentalapp.cfg;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// CompositeHealthContributor

@Component("Database")
@RequiredArgsConstructor
public class DatabaseHealthContributor implements HealthContributor, HealthIndicator {
    private final DataSource dataSource;

    @Override
    public Health health() {
        try(Connection con = dataSource.getConnection()) {
            Statement statement = con.createStatement();
            statement.executeQuery("select * from vehicles");
        } catch (SQLException ex) {
               return Health.outOfService().withException(ex).build();
        }
        return Health.up().build();
    }
}
