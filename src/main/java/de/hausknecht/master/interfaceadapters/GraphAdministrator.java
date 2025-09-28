package de.hausknecht.master.interfaceadapters;

import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.TransitionTriple;
import de.hausknecht.master.entity.service.GraphRendering;
import de.hausknecht.master.entity.service.NodeDefinitionRegistry;
import de.hausknecht.master.entity.service.NodeRegistry;
import de.hausknecht.master.entity.service.TransitionRegistry;
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

    public Optional<String> returnGraphDefinition() {
        List<String> availableNodes = nodeRegistry.getNodes();
        String startingNode = nodeDefinitionRegistry.getStartingNode();
        List<String> endingNodes = nodeDefinitionRegistry.getEndingNodes();
        List<TransitionTriple> transitions = transitionRegistry.getTransitions();

        GraphData graphData = new GraphData(availableNodes, startingNode, endingNodes, transitions);

        return graphRendering.returnGraphDefinition(graphData, Optional.empty());
    }
}
