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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.prefs.Preferences;

import static de.hausknecht.master.frameworksanddrivers.persistence.SessionTime.KEY;
import static org.mockito.Mockito.*;

class SessionTimeTest {

    private final Preferences preferencesMock = mock();
    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final SessionTime sessionTime = new SessionTime(preferencesMock, applicationEventPublisherMock);

    @Nested
    class GetTotalDuration {

        @Test
        void getTotalDuration() {
            sessionTime.getTotalDuration();

            verify(preferencesMock, times(1)).getLong(eq(KEY), eq(0L));
        }
    }

    @Nested
    class EndSession {

        @Test
        void endSessionBeforeStart() {
            sessionTime.endSession();

            verify(preferencesMock, times(0)).putLong(eq(KEY), eq(0L));
            verify(applicationEventPublisherMock, times(0)).publishEvent(new UpdatedTimeEvent());
        }

        @Test
        void endSessionAfterStart() {
            sessionTime.startSession();
            sessionTime.endSession();

            verify(preferencesMock, times(1)).putLong(eq(KEY), anyLong());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new UpdatedTimeEvent());
        }
    }
}
