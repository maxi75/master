package de.hausknecht.master.entity.domain.automata;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static de.hausknecht.master.ConstantProvider.COMMA;

public record GraphData(List<String> availableNodes, String startingNode, List<String> endingNodes, List<TransitionTriple> transitions) {
    static final String ERROR_FETCHING_START = "Error: Fehler beim Ermitteln des Startzustandes";
    static final String ERROR_FETCHING_NODES = "Error: Fehler beim Ermitteln der Zustände";
    static final String ERROR_FETCHING_TRANSITIONS = "Error: Fehler beim Ermitteln der Transitions";

    public String getStartingNodeAsString() {
        return startingNode != null ? startingNode : ERROR_FETCHING_START;
    }

    public String getEndingNodeAsString() {
        return getNodesAsString(endingNodes);
    }

    public String getNodesAsString() {
        return getNodesAsString(endingNodes);
    }

    private String getNodesAsString(List<String> nodes) {
        return nodes != null ? String.join(COMMA, nodes) : ERROR_FETCHING_NODES;
    }

    public String getAlphabetAsString() {
        if (transitions == null) return ERROR_FETCHING_TRANSITIONS;

        Set<String> alphabet = fetchAlphabet();
        return String.join(COMMA, alphabet);
    }

    private Set<String> fetchAlphabet() {
        return transitions.stream()
                .map(TransitionTriple::transitionWord)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
