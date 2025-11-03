package de.hausknecht.master.entity.domain.automata;

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
        return getNodesAsString(availableNodes);
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
                .filter(Objects::nonNull)
                .map(TransitionTriple::transitionWord)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
