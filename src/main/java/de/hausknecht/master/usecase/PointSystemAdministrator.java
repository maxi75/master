package de.hausknecht.master.usecase;

import de.hausknecht.master.frameworksanddrivers.persistence.PointSystem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PointSystemAdministrator {
    private final PointSystem pointSystem;

    public void addPoints(int points) {
        pointSystem.addPoints(points);
    }

    public void subtractPoints(int points) {
        pointSystem.subtractPoints(points);
    }

    public int getPoints() {
        return pointSystem.getTotalPoints();
    }
}
