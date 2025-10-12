package de.hausknecht.master.entity.service.automata.registries;

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
            assertEquals(DFA, classUnderTest.getSelectedGraph());

            classUnderTest.changeSelectedGraph(NFA);

            assertEquals(NFA, classUnderTest.getSelectedGraph());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
        }

        @Test
        void selectedGraphIsNull() {
            assertEquals(DFA, classUnderTest.getSelectedGraph());

            classUnderTest.changeSelectedGraph(null);

            assertNull(classUnderTest.getSelectedGraph());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());

        }
    }
}