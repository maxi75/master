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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.prefs.Preferences;

import static de.hausknecht.master.frameworksanddrivers.persistence.PointSystem.KEY;
import static org.mockito.Mockito.*;

class PointSystemTest {

    private final Preferences preferencesMock = mock();
    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final PointSystem pointSystem = new PointSystem(preferencesMock, applicationEventPublisherMock);

    @Nested
    class GetTotalPoints {

        @Test
        void getDefaultTotalPoints() {
            pointSystem.getTotalPoints();
            verify(preferencesMock, times(1)).getInt(anyString(), anyInt());
        }
    }

    @Nested
    class AddPoints {

        @Test
        void addPoints() {
            pointSystem.addPoints(0);
            verify(preferencesMock, times(1)).putInt(eq(KEY), anyInt());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new PointsChangedEvent(ConstantProvider.PLUS + 0, true));
        }
    }

    @Nested
    class SubtractPoints {

        @Test
        void subtractPoints() {
            pointSystem.subtractPoints(0);
            verify(preferencesMock, times(1)).putInt(eq(KEY), anyInt());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new PointsChangedEvent(ConstantProvider.MINUS + 0, false));
        }
    }
}
