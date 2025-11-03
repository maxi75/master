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

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.service.automata.graph.GraphRendering;
import de.hausknecht.master.entity.service.automata.registries.GraphRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeDefinitionRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeRegistry;
import de.hausknecht.master.entity.service.automata.registries.TransitionRegistry;
import de.hausknecht.master.entity.service.content.Simulator;
import lombok.AllArgsConstructor;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class GraphAdministrator {
    static final String ERROR_SEARCHING_SOLUTIONS = "Error while searching for valid solutions.";

    private final NodeRegistry nodeRegistry;
    private final NodeDefinitionRegistry nodeDefinitionRegistry;
    private final TransitionRegistry transitionRegistry;
    private final GraphRendering graphRendering;
    private final GraphRegistry graphRegistry;
    private final Simulator simulator;

    public Optional<String> returnGraphDefinition() {
        GraphData graphData = getGraphDataFromCurrentSimulation();

        return graphRegistry.getSelectedGraph().equals(AutomataSimulation.DEA) ?
                graphRendering.dfaToDot(graphData) :
                graphRendering.nfaToDot(graphData);
    }

    public CompactDFA<String> getCompactDFAFromCurrentSimulation() {
        return getCompactDFAFromGraphData(getGraphDataFromCurrentSimulation());
    }

    public CompactDFA<String> getCompactDFAFromGraphData(GraphData graphData) {
        return graphRendering.getCompactDFAFromGraphData(graphData);
    }

    public Optional<String> returnSimulatedGraphDefinition(String input) {
        GraphData graphData = getGraphDataFromCurrentSimulation();

        return graphRegistry.getSelectedGraph().equals(AutomataSimulation.DEA) ?
                graphRendering.simulatedDFAToDot(graphData, input) :
                graphRendering.simulatedNFAToDot(graphData, input);
    }

    private GraphData getGraphDataFromCurrentSimulation() {
        List<String> availableNodes = nodeRegistry.getNodes();
        String startingNode = nodeDefinitionRegistry.getStartingNode();
        List<String> endingNodes = nodeDefinitionRegistry.getEndingNodes();
        List<TransitionTriple> transitions = transitionRegistry.getTransitions();

        return new GraphData(availableNodes, startingNode, endingNodes, transitions);
    }

    public void changeSelectedGraph(AutomataSimulation automataSimulation) {
        graphRegistry.changeSelectedGraph(automataSimulation);
    }

    public boolean isInputAccepted(GraphData graphData, String input) {
        return graphRendering.acceptsInputNFA(graphData, input);
    }

    public String findAcceptingInputForGraphData(GraphData graphData) {
        Optional<String> result = simulator.findAcceptingInputForGraphData(graphData);
        return result.orElse(ERROR_SEARCHING_SOLUTIONS);
    }
}
