package org.example.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import javax.sql.DataSource;
import java.sql.Connection;

@Liveness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

  @Inject DataSource dataSource;

  @Override
  public HealthCheckResponse call() {
    try (final Connection connection = dataSource.getConnection()) {
      if (connection.isValid(1)) { // Überprüfe, ob die Verbindung gültig ist
        return HealthCheckResponse.up("Database connection");
      } else {
        return HealthCheckResponse.down("Database connection is invalid");
      }
    } catch (final Exception e) {
      return HealthCheckResponse.down("Database connection error: " + e.getMessage());
    }
  }
}
