package de.hausknecht.master.interfaceadapters;

import de.hausknecht.master.entity.service.persistence.PointSystem;
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
