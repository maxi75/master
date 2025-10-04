package de.hausknecht.master.entity.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record GraphData(List<String> availableNodes, String startingNode, List<String> endingNodes, List<TransitionTriple> transitions) {

    public String getStartingNodeAsString() {
        return startingNode != null ? startingNode : "Error: Fehler beim Ermitteln des Startzustandes";
    }

    public String getEndingNodeAsString() {
        return getNodesAsString(endingNodes);
    }

    public String getNodesAsString() {
        return getNodesAsString(endingNodes);
    }

    private String getNodesAsString(List<String> nodes) {
        return nodes != null ? String.join(", ", nodes) : "Error: Fehler beim Ermitteln der Zustände";
    }

    public String ermittleAlphabetAsString() {
        if (transitions == null) return "Error: Fehler beim Ermitteln der Transitions";

        Set<String> alphabet = ermittleAlphabet();
        return String.join(", ", alphabet);
    }

    private Set<String> ermittleAlphabet() {
        return transitions.stream()
                .map(TransitionTriple::transitionWord)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
