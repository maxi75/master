package de.hausknecht.master.entity.domain.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.prefs.Preferences;

@Configuration
public class PreferencesConfig {

    private static final String BASE = "de.hausknecht.master";

    @Bean @Qualifier("pointsPreferences")
    public Preferences getPointsPreferences() {
        return Preferences.userRoot().node(BASE + "/points");
    }

    @Bean @Qualifier("sessionPreferences")
    public Preferences getSessionPreferences() {
        return Preferences.userRoot().node(BASE + "/session");
    }
}
