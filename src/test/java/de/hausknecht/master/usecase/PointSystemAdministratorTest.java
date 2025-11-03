package de.hausknecht.master.usecase;

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
