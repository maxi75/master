package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.TransitionTriple;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GraphRendering {

    private static final String GRAPH_DEFINITION = """
           digraph Automaton {
             rankdir=LR;
             labelloc="t";
             fontsize=20;
             node [fontname="Arial"];
             edge [fontname="Arial"];
           \s
           %s
           \s
           %s
           \s
           %s
           }
           """;

    public Optional<String> returnGraphDefinition(GraphData graphData, Optional<String> highlightedNode) {
        if (!preconditionsAreFulfilled(graphData)) return Optional.empty();

        String startSection = buildStartSection(graphData.startingNode());
        String nodesSection = buildNodesSection(graphData.availableNodes(), graphData.endingNodes(),  highlightedNode);
        String edgesSection = buildEdgeSection(graphData.transitions());
        return Optional.of(String.format(GRAPH_DEFINITION, startSection, nodesSection, edgesSection));
    }

    private boolean preconditionsAreFulfilled(GraphData graphData) {
        return atLeastOneNodeInList(graphData.availableNodes());
    }

    private boolean atLeastOneNodeInList(List<String> nodes) {
        return nodes != null &&
                !nodes.isEmpty() &&
                nodes.stream().allMatch(node -> node != null && !node.isBlank());
    }

    private String buildStartSection(String startingNode) {
        if (startingNode == null) return "";
        return "  __start__ [shape=point, width=0.1, label=\"\"]; \n  __start__ -> \"" + startingNode + "\";";
    }

    private String buildNodesSection(List<String> availableNodes, List<String> endingNodes, Optional<String> highlightedNode) {
        return availableNodes.stream()
            .map(node -> {
                String shape = endingNodes.contains(node) ? "doublecircle" : "circle";

                String highlightAttribute = highlightedNode.isPresent() && highlightedNode.get().equals(node) ?
                        ",style=filled,fillcolor=\"ffe5e5\",color=\"#cc0000\",fontcolor=\"#990000\",penwidth=2" : "";

                return "  \"" + node + "\" [shape=" + shape + highlightAttribute + "];";
            }).collect(Collectors.joining("\n"));
    }

    private String buildEdgeSection(List<TransitionTriple> transitions) {
        return transitions.stream().map(transition ->
                "  \"" + transition.fromNode() +
                "\" -> \"" + transition.toNode() +
                "\" [label=\"" + transition.transitionWord() + "\"];")
            .collect(Collectors.joining("\n"));
    }
}
