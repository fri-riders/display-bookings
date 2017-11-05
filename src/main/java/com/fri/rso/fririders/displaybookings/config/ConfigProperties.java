package com.fri.rso.fririders.displaybookings.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("bookings-config")
public class ConfigProperties {

    @ConfigValue(value = "insert-enabled", watch = true)
    private boolean insertEnabled;

    public boolean isInsertEnabled() {
        return insertEnabled;
    }

    public void setInsertEnabled(boolean insertEnabled) {
        this.insertEnabled = insertEnabled;
    }
}
