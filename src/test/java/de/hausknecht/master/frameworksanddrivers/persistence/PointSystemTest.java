package de.hausknecht.master.frameworksanddrivers.persistence;

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