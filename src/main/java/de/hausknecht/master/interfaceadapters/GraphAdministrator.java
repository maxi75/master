package de.hausknecht.master.interfaceadapters;

import de.hausknecht.master.entity.domain.AutomataSimulation;
import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.TransitionTriple;
import de.hausknecht.master.entity.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GraphAdministrator {
    private final NodeRegistry nodeRegistry;
    private final NodeDefinitionRegistry nodeDefinitionRegistry;
    private final TransitionRegistry transitionRegistry;
    private final GraphRendering graphRendering;
    private final GraphRegistry graphRegistry;
    private final Simulator simulator;

    public Optional<String> returnGraphDefinition() {
        List<String> availableNodes = nodeRegistry.getNodes();
        String startingNode = nodeDefinitionRegistry.getStartingNode();
        List<String> endingNodes = nodeDefinitionRegistry.getEndingNodes();
        List<TransitionTriple> transitions = transitionRegistry.getTransitions();

        GraphData graphData = new GraphData(availableNodes, startingNode, endingNodes, transitions);

        return graphRegistry.getSelectedGraph().equals(AutomataSimulation.DFA) ?
                graphRendering.dfaToDot(graphData) :
                graphRendering.nfaToDot(graphData);
    }

    public Optional<String> returnSimulatedGraphDefinition(String input) {
        List<String> availableNodes = nodeRegistry.getNodes();
        String startingNode = nodeDefinitionRegistry.getStartingNode();
        List<String> endingNodes = nodeDefinitionRegistry.getEndingNodes();
        List<TransitionTriple> transitions = transitionRegistry.getTransitions();

        GraphData graphData = new GraphData(availableNodes, startingNode, endingNodes, transitions);

        return graphRegistry.getSelectedGraph().equals(AutomataSimulation.DFA) ?
                graphRendering.simulatedDFAToDot(graphData, input) :
                graphRendering.simulatedNFAToDot(graphData, input);
    }

    public void changeSelectedGraph(AutomataSimulation automataSimulation) {
        graphRegistry.changeSelectedGraph(automataSimulation);
    }

    public boolean isInputAccepted(GraphData graphData, String input, AutomataSimulation automata) {
        return automata == AutomataSimulation.DFA ?
                graphRendering.acceptsInputDFA(graphData, input) :
                graphRendering.acceptsInputNFA(graphData, input);
    }

    public String findAcceptingInputForGraphData(GraphData graphData) {
        Optional<String> result = simulator.findAcceptingInputForGraphData(graphData);
        return result.orElse("Error while searching for valid solutions.");
    }
}
