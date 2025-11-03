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

import de.hausknecht.master.ConstantProvider;
import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.prefs.Preferences;

@Service
public class PointSystem {
    static final String KEY = "points.total";
    private final Preferences preferences;

    private final ApplicationEventPublisher publisher;

    public PointSystem(@Qualifier("pointsPreferences") Preferences preferences, ApplicationEventPublisher publisher) {
        this.preferences = preferences;
        this.publisher = publisher;
    }

    public int getTotalPoints() {
        return preferences.getInt(KEY, 0);
    }

    public void addPoints(int points) {
        preferences.putInt(KEY, getTotalPoints() + points);
        publisher.publishEvent(new PointsChangedEvent(ConstantProvider.PLUS + points, true));
    }

    public void subtractPoints(int points) {
        preferences.putInt(KEY, getTotalPoints() - points);
        publisher.publishEvent(new PointsChangedEvent(ConstantProvider.MINUS + points, false));
    }
}
