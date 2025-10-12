package de.hausknecht.master.frameworksanddrivers.persistence;

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