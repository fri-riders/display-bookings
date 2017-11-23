package com.fri.rso.fririders.displaybookings.health;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class DisplayBookingsServiceHealthCheck implements HealthCheck {

    @Inject
    private ConfigProperties configProperties;

    @Override
    public HealthCheckResponse call() {

        if (configProperties.isHealthy()) {
            return HealthCheckResponse.named(DisplayBookingsServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(DisplayBookingsServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }

}
