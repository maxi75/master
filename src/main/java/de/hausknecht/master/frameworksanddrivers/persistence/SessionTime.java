package de.hausknecht.master.frameworksanddrivers.persistence;

import de.hausknecht.master.entity.domain.eventdata.UpdatedTimeEvent;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

@Service
@RequiredArgsConstructor
public class SessionTime {
    private static final String KEY = "time.total.ms";
    private static final String NAME = "session-timer";

    private final Preferences preferences = Preferences.userNodeForPackage(this.getClass());

    private volatile long sessionStart = -1L;
    private Timer timer;

    private final ApplicationEventPublisher applicationEventPublisher;

    public synchronized void startSession() {
        if (sessionStart != -1) return;
        sessionStart = System.currentTimeMillis();
        startTimer();
    }

    public Duration getTotalDuration() {
        long total = preferences.getLong(KEY, 0L);
        long running = (sessionStart != -1) ? System.currentTimeMillis() - sessionStart : 0;
        return Duration.millis(total + running);
    }

    public synchronized void endSession() {
        if (sessionStart == -1) return;

        long current =  System.currentTimeMillis();
        addTimeToTotal(current - sessionStart);
        sessionStart = -1;
        timer.cancel();
        timer = null;
    }

    private void startTimer() {
        timer = new Timer(NAME, true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (SessionTime.this) {
                    if (sessionStart == -1) return;

                    long currentTime = System.currentTimeMillis();
                    addTimeToTotal(currentTime - sessionStart);
                    sessionStart = currentTime;
                }
            }
        }, 5000, 5000);
    }

    private void addTimeToTotal(long milliseconds) {
        if (milliseconds < 0) return;
        long total = preferences.getLong(KEY, 0);
        preferences.putLong(KEY, total + milliseconds);
        applicationEventPublisher.publishEvent(new UpdatedTimeEvent());
    }
}
