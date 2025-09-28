package de.hausknecht.master.entity.domain;

import java.util.List;

public record GraphData(List<String> availableNodes, String startingNode, List<String> endingNodes, List<TransitionTriple> transitions) {
}
