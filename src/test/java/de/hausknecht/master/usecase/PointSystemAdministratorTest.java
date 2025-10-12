package de.hausknecht.master.usecase;

import de.hausknecht.master.frameworksanddrivers.persistence.PointSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PointSystemAdministratorTest {

    private final PointSystem pointSystemMock = mock();
    private final PointSystemAdministrator classUnderTest = new PointSystemAdministrator(pointSystemMock);

    @BeforeEach
    void setUp() {
        when(pointSystemMock.getTotalPoints()).thenReturn(10);
    }

    @Nested
    class AddPoints {

        @Test
        void addPoints() {
            classUnderTest.addPoints(20);

            verify(pointSystemMock, times(1)).addPoints(20);
        }
    }

    @Nested
    class SubtractPoints {

        @Test
        void subtractPoints() {
            classUnderTest.subtractPoints(20);

            verify(pointSystemMock, times(1)).subtractPoints(20);
        }
    }

    @Nested
    class GetPoints {

        @Test
        void getPoints() {
            classUnderTest.getPoints();

            verify(pointSystemMock, times(1)).getTotalPoints();
        }
    }
}