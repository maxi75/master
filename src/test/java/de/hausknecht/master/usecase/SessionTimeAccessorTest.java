package de.hausknecht.master.usecase;

import de.hausknecht.master.frameworksanddrivers.persistence.SessionTime;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SessionTimeAccessorTest {

    private final SessionTime sessionTimeMock = mock();
    private final SessionTimeAccessor classUnderTest = new SessionTimeAccessor(sessionTimeMock);

    @BeforeEach
    void setUp() {
        when(sessionTimeMock.getTotalDuration()).thenReturn(Duration.ZERO);
    }

    @Nested
    class GetSessionTime {

        @Test
        void getSessionTime() {
            classUnderTest.getSessionTime();

            verify(sessionTimeMock, times(1)).getTotalDuration();
        }
    }
}