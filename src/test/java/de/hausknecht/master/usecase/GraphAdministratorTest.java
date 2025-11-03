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

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.service.automata.graph.GraphRendering;
import de.hausknecht.master.entity.service.automata.registries.GraphRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeDefinitionRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeRegistry;
import de.hausknecht.master.entity.service.automata.registries.TransitionRegistry;
import de.hausknecht.master.entity.service.content.Simulator;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.hausknecht.master.usecase.GraphAdministrator.ERROR_SEARCHING_SOLUTIONS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GraphAdministratorTest {

    private final NodeRegistry nodeRegistryMock = mock();
    private final NodeDefinitionRegistry nodeDefinitionRegistryMock = mock();
    private final TransitionRegistry transitionRegistryMock = mock();
    private final GraphRendering graphRenderingMock = mock();
    private final GraphRegistry graphRegistryMock = mock();
    private final Simulator simulatorMock = mock();

    private final GraphAdministrator classUnderTest = new GraphAdministrator(nodeRegistryMock, nodeDefinitionRegistryMock, transitionRegistryMock,
            graphRenderingMock, graphRegistryMock, simulatorMock);

    @BeforeEach
    void setUp() {
        when(nodeRegistryMock.getNodes()).thenReturn(FXCollections.observableArrayList());

        when(nodeDefinitionRegistryMock.getStartingNode()).thenReturn("");
        when(nodeDefinitionRegistryMock.getEndingNodes()).thenReturn(FXCollections.observableArrayList());

        when(transitionRegistryMock.getTransitions()).thenReturn(FXCollections.observableArrayList());

        when(graphRegistryMock.getSelectedGraph()).thenReturn(AutomataSimulation.DEA);

        when(graphRenderingMock.dfaToDot(any())).thenReturn(Optional.empty());
        when(graphRenderingMock.nfaToDot(any())).thenReturn(Optional.empty());
        when(graphRenderingMock.simulatedDFAToDot(any(), anyString())).thenReturn(Optional.empty());
        when(graphRenderingMock.simulatedNFAToDot(any(), anyString())).thenReturn(Optional.empty());
        when(graphRenderingMock.getCompactDFAFromGraphData(any())).thenReturn(TestDataGenerator.getCorrectDfa(true, true, true, true));
        when(graphRenderingMock.acceptsInputNFA(any(), anyString())).thenReturn(true);

        when(simulatorMock.findAcceptingInputForGraphData(any())).thenReturn(Optional.of(""));
    }

    @Nested
    class ReturnGraphDefinition {

        @Test
        void returnGraphDefinitionDfa() {
            classUnderTest.returnGraphDefinition();

            verify(nodeRegistryMock, times(1)).getNodes();
            verify(nodeDefinitionRegistryMock, times(1)).getStartingNode();
            verify(nodeDefinitionRegistryMock, times(1)).getEndingNodes();
            verify(transitionRegistryMock, times(1)).getTransitions();
            verify(graphRegistryMock, times(1)).getSelectedGraph();
            verify(graphRenderingMock, times(1)).dfaToDot(any());
            verify(graphRenderingMock, times(0)).nfaToDot(any());
        }

        @Test
        void returnGraphDefinitionNfa() {
            when(graphRegistryMock.getSelectedGraph()).thenReturn(AutomataSimulation.NEA);
            classUnderTest.returnGraphDefinition();

            verify(nodeRegistryMock, times(1)).getNodes();
            verify(nodeDefinitionRegistryMock, times(1)).getStartingNode();
            verify(nodeDefinitionRegistryMock, times(1)).getEndingNodes();
            verify(transitionRegistryMock, times(1)).getTransitions();
            verify(graphRegistryMock, times(1)).getSelectedGraph();
            verify(graphRenderingMock, times(0)).dfaToDot(any());
            verify(graphRenderingMock, times(1)).nfaToDot(any());
        }
    }

    @Nested
    class GetCompactDFAFromCurrentSimulation {

        @Test
        void getCompactDFAFromCurrentSimulation() {
            classUnderTest.getCompactDFAFromCurrentSimulation();

            verify(graphRenderingMock, times(1)).getCompactDFAFromGraphData(any());
        }
    }

    @Nested
    class GetCompactDFAFromGraphData {

        @Test
        void getCompactDFAFromGraphData() {
            classUnderTest.getCompactDFAFromGraphData(TestDataGenerator.getCorrectGraphData());

            verify(graphRenderingMock, times(1)).getCompactDFAFromGraphData(any());
        }
    }

    @Nested
    class ReturnSimulatedGraphDefinition {

        @Test
        void returnSimulatedGraphDefinitionDfa() {
            classUnderTest.returnSimulatedGraphDefinition("input");

            verify(nodeRegistryMock, times(1)).getNodes();
            verify(nodeDefinitionRegistryMock, times(1)).getStartingNode();
            verify(nodeDefinitionRegistryMock, times(1)).getEndingNodes();
            verify(transitionRegistryMock, times(1)).getTransitions();
            verify(graphRegistryMock, times(1)).getSelectedGraph();
            verify(graphRenderingMock, times(1)).simulatedDFAToDot(any(), anyString());
            verify(graphRenderingMock, times(0)).simulatedNFAToDot(any(), anyString());
        }

        @Test
        void returnSimulatedGraphDefinitionNfa() {
            when(graphRegistryMock.getSelectedGraph()).thenReturn(AutomataSimulation.NEA);
            classUnderTest.returnSimulatedGraphDefinition("input");

            verify(nodeRegistryMock, times(1)).getNodes();
            verify(nodeDefinitionRegistryMock, times(1)).getStartingNode();
            verify(nodeDefinitionRegistryMock, times(1)).getEndingNodes();
            verify(transitionRegistryMock, times(1)).getTransitions();
            verify(graphRegistryMock, times(1)).getSelectedGraph();
            verify(graphRenderingMock, times(0)).simulatedDFAToDot(any(), anyString());
            verify(graphRenderingMock, times(1)).simulatedNFAToDot(any(), anyString());
        }
    }

    @Nested
    class ChangeSelectedGraph {

        @Test
        void changeSelectedGraph() {
            classUnderTest.changeSelectedGraph(AutomataSimulation.NEA);

            verify(graphRegistryMock, times(1)).changeSelectedGraph(any());
        }
    }

    @Nested
    class IsInputAccepted {

        @Test
        void isInputAcceptedWithNFA() {
            GraphData graphData = TestDataGenerator.getCorrectGraphData();
            classUnderTest.isInputAccepted(graphData, "input");

            verify(graphRenderingMock, times(1)).acceptsInputNFA(any(), anyString());
        }
    }

    @Nested
    class FindAcceptingInputForGraphData {

        @Test
        void findAcceptingInputForGraphDataWithValue() {
            GraphData graphData = TestDataGenerator.getCorrectGraphData();
            assertEquals("", classUnderTest.findAcceptingInputForGraphData(graphData));
        }

        @Test
        void findAcceptingInputForGraphDataWithoutValue() {
            when(simulatorMock.findAcceptingInputForGraphData(any())).thenReturn(Optional.empty());
            GraphData graphData = TestDataGenerator.getCorrectGraphData();
            assertEquals(ERROR_SEARCHING_SOLUTIONS, classUnderTest.findAcceptingInputForGraphData(graphData));
        }
    }
}
