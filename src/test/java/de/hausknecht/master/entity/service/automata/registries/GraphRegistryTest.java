package de.hausknecht.master.entity.service.automata.registries;

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

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static de.hausknecht.master.entity.domain.automata.AutomataSimulation.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphRegistryTest {

    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final GraphRegistry classUnderTest = new GraphRegistry(applicationEventPublisherMock);

    @Nested
    class ChangeSelectedGraph {

        @Test
        void withValue() {
            assertEquals(NEA, classUnderTest.getSelectedGraph());

            classUnderTest.changeSelectedGraph(NEA);

            assertEquals(NEA, classUnderTest.getSelectedGraph());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void selectedGraphIsNull() {
            assertEquals(NEA, classUnderTest.getSelectedGraph());

            classUnderTest.changeSelectedGraph(null);

            assertNull(classUnderTest.getSelectedGraph());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());

        }
    }
}
