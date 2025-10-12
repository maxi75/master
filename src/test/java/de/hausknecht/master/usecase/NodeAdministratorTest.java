package de.hausknecht.master.usecase;

import de.hausknecht.master.entity.service.automata.registries.NodeDefinitionRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeRegistry;
import de.hausknecht.master.entity.service.automata.registries.TransitionRegistry;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NodeAdministratorTest {

    private final NodeRegistry nodeRegistryMock = mock();
    private final NodeDefinitionRegistry nodeDefinitionRegistryMock = mock();
    private final TransitionRegistry transitionRegistryMock = mock();

    private final NodeAdministrator classUnderTest = new NodeAdministrator(nodeRegistryMock, nodeDefinitionRegistryMock, transitionRegistryMock);

    @BeforeEach
    void setUp() {
        when(nodeRegistryMock.getNodes()).thenReturn(FXCollections.observableArrayList());
        when(nodeDefinitionRegistryMock.addNode(anyString())).thenReturn(true);
    }

    @Nested
    class RemoveNodeFromNodeRegistry {

        @Test
        void removeNodeFromRegistry() {
            classUnderTest.removeNodeFromNodeRegistry("node");

            verify(nodeRegistryMock, times(1)).removeNode(anyString());
            verify(nodeDefinitionRegistryMock, times(1)).removeNode(anyString());
            verify(transitionRegistryMock, times(1)).removeAllTransitionsThatContainNode(anyString());
        }
    }

    @Nested
    class RemoveNodeFromTransitionRegistry {

        @Test
        void removeNodeFromTransitionRegistry() {
            classUnderTest.removeNodeFromTransitionRegistry(null);
            verify(transitionRegistryMock, times(1)).removeTransition(any());
        }
    }

    @Nested
    class AddTransition {

        @Test
        void addTransition() {
            classUnderTest.addTransition(null);
            verify(transitionRegistryMock, times(1)).addTransition(any());
        }
    }

    @Nested
    class AddNode {

        @Test
        void addNode() {
            classUnderTest.addNode(null);
            verify(nodeRegistryMock, times(1)).addNode(any());
        }
    }

    @Nested
    class GetNodes {

        @Test
        void getNodes() {
            classUnderTest.getNodes();
            verify(nodeRegistryMock, times(1)).getNodes();
        }
    }

    @Nested
    class SetStartingNode {

        @Test
        void setStartingNode() {
            classUnderTest.setStartingNode(null);
            verify(nodeDefinitionRegistryMock, times(1)).setStartingNode(any());
        }
    }

    @Nested
    class AddEndingNode {

        @Test
        void addEndingNode() {
            classUnderTest.addEndingNode(null);
            verify(nodeDefinitionRegistryMock, times(1)).addNode(any());
        }
    }

    @Nested
    class RemoveEndingNode {

        @Test
        void removeEndingNode() {
            classUnderTest.removeEndingNode(null);
            verify(nodeDefinitionRegistryMock, times(1)).removeNode(null);
        }
    }
}