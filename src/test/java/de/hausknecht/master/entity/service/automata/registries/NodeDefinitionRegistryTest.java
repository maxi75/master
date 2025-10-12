package de.hausknecht.master.entity.service.automata.registries;

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemovedEvent;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeDefinitionRegistryTest {

    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final NodeDefinitionRegistry classUnderTest = new NodeDefinitionRegistry(applicationEventPublisherMock);

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
        void nameIsAlreadyPartOfEndingNodes() {
            classUnderTest.addNode(NODE_NAME);
            boolean actual = classUnderTest.addNode(NODE_NAME);

            assertFalse(actual);
        }
    }

    @Nested
    class RemoveNode {

        @Test
        void nodeIsNotPartOfEndingNodes() {
            assertThatNoException().isThrownBy(() -> classUnderTest.removeNode(NODE_NAME));

            verify(applicationEventPublisherMock, times(0)).publishEvent(new EndingNodeRemovedEvent(anyString()));
            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void nodeIsPartOfEndingNodes() {
            classUnderTest.addNode(NODE_NAME);
            assertThatNoException().isThrownBy(() -> classUnderTest.removeNode(NODE_NAME));

            verify(applicationEventPublisherMock, times(1)).publishEvent(new EndingNodeRemovedEvent(NODE_NAME));
            verify(applicationEventPublisherMock, times(2)).publishEvent(new GraphChangedEvent());
        }
    }

    @Nested
    class SetStartingNode {

        @Test
        void withValue() {
            classUnderTest.setStartingNode(NODE_NAME);

            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void nameIsNull() {
            classUnderTest.setStartingNode(null);

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());

        }

        @Test
        void nameIsBlank() {
            classUnderTest.setStartingNode(" ");

            verify(applicationEventPublisherMock, times(0)).publishEvent(new GraphChangedEvent());
        }
    }
}