package de.hausknecht.master.frameworksanddrivers.persistence;

/*-
 * #%L
 * master
 * %%
 * Copyright (C) 2025 Maximilian Hausknecht
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.hausknecht.master.entity.domain.eventdata.UpdatedTimeEvent;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

@Service
public class SessionTime {
    static final String KEY = "time.total.ms";
    private static final String NAME = "session-timer";

    private final Preferences preferences;

    private volatile long sessionStart = -1L;
    private Timer timer;

    private final ApplicationEventPublisher applicationEventPublisher;

    public SessionTime(@Qualifier("sessionPreferences") Preferences preferences, ApplicationEventPublisher publisher) {
        this.preferences = preferences;
        this.applicationEventPublisher = publisher;
    }

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
