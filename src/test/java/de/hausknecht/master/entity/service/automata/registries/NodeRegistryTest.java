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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NodeRegistryTest {

    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final NodeRegistry classUnderTest = new NodeRegistry(applicationEventPublisherMock);

    private static final String NODE_NAME = "NodeName";

    @Nested
    class AddNode {

        @Test
        void withValue() {
            boolean actual = classUnderTest.addNode(NODE_NAME);

            assertTrue(actual);
        }

        @Test
        void nameIsNull() {
            boolean actual = classUnderTest.addNode(null);

            assertFalse(actual);
        }

        @Test
        void nameIsBlank() {
            boolean actual = classUnderTest.addNode(" ");

            assertFalse(actual);
        }

        @Test
        void nameIsAlreadyPartOfNodes() {
            classUnderTest.addNode(NODE_NAME);
            boolean actual = classUnderTest.addNode(NODE_NAME);

            assertFalse(actual);
        }
    }

    @Nested
    class RemoveNode {

        @Test
        void withValue() {
            classUnderTest.addNode(NODE_NAME);
            classUnderTest.removeNode(NODE_NAME);

            verify(applicationEventPublisherMock, times(2)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void nameIsNull() {
            classUnderTest.addNode(NODE_NAME);
            classUnderTest.removeNode(null);

            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());

        }

        @Test
        void nameIsBlank() {
            classUnderTest.addNode(NODE_NAME);
            classUnderTest.removeNode(" ");

            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
        }
    }
}
