package de.hausknecht.master.interfaceadapters;

import de.hausknecht.master.entity.service.persistence.SessionTime;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SessionTimeAccessor {

    private final SessionTime sessionTime;

    public Duration getSessionTime(){
        return sessionTime.getTotalDuration();
    }
}
