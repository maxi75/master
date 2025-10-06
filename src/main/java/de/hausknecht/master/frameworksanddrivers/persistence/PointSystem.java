package de.hausknecht.master.frameworksanddrivers.persistence;

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;

@Service
@AllArgsConstructor
public class PointSystem {
    private static final String KEY = "points.total";
    private final Preferences preferences = Preferences.userNodeForPackage(this.getClass());

    private final ApplicationEventPublisher publisher;

    public int getTotalPoints() {
        return preferences.getInt(KEY, 0);
    }

    public void addPoints(int points) {
        preferences.putInt(KEY, getTotalPoints() + points);
        publisher.publishEvent(new PointsChangedEvent("+ " + points, true));
    }

    public void subtractPoints(int points) {
        preferences.putInt(KEY, getTotalPoints() - points);
        publisher.publishEvent(new PointsChangedEvent("- " + points, false));
    }
}
