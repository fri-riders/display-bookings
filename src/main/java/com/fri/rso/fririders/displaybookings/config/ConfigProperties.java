package com.fri.rso.fririders.displaybookings.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("bookings-config")
public class ConfigProperties {

    @ConfigValue(value = "insert-enabled", watch = true)
    private boolean insertEnabled;

    @ConfigValue(value = "healthy", watch = true)
    private boolean healthy;

    public boolean isInsertEnabled() {
        return insertEnabled;
    }

    public void setInsertEnabled(boolean insertEnabled) {
        this.insertEnabled = insertEnabled;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

}
