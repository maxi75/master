package de.hausknecht.master.entity.service.automata.graph;

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

import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.GraphEvaluationResult;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import de.hausknecht.master.entity.service.automata.definition.AutomataGenerator;
import de.hausknecht.master.entity.service.content.Simulator;
import lombok.AllArgsConstructor;
import net.automatalib.alphabet.Alphabet;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import net.automatalib.util.automaton.fsa.NFAs;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class GraphRendering {

    private final ToDotConverter toDotConverter;
    private final AutomataGenerator automataGenerator;
    private final Simulator simulator;

    public Optional<String> dfaToDot(GraphData graphData) {
        return simulatedDFAToDot(graphData, null);
    }

    public Optional<String> simulatedDFAToDot(GraphData graphData, String input) {
        if (graphData == null || graphData.startingNode() == null || graphData.startingNode().isBlank() ||
            graphData.endingNodes() == null || graphData.endingNodes().isEmpty()) return Optional.empty();

        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        if (nfaValues == null || nfaValues.nodeToId().isEmpty()) return Optional.empty();

        CompactDFA<String> dfa = getDFAFromNFA(nfaValues.nfa());
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedDFA(dfa, input);
        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.ofNullable(toDotConverter.toDot(dfa, null, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public CompactDFA<String> getCompactDFAFromGraphData(GraphData graphData) {
        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        return nfaValues != null ? getDFAFromNFA(nfaValues.nfa()) : null;
    }

    private CompactDFA<String> getDFAFromNFA(CompactNFA<String> compactNFA) {
        Alphabet<String> alphabet = compactNFA.getInputAlphabet();
        CompactDFA<String> dfa = NFAs.determinize(compactNFA, alphabet);
        return automataGenerator.removeSinkNodes(dfa);
    }

    public Optional<String> nfaToDot(GraphData graphData) {
        return simulatedNFAToDot(graphData, null);
    }

    public Optional<String> simulatedNFAToDot(GraphData graphData, String input) {
        if (graphData == null) return Optional.empty();

        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedNFA(nfaValues.nfa(), input);

        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.ofNullable(toDotConverter.toDot(null, nfaValues, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean acceptsInputNFA(GraphData graphData, String input) {
        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        if (nfaValues == null) return false;

        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedNFA(nfaValues.nfa(), input);
        return Boolean.TRUE.equals(simulationResult.map(GraphEvaluationResult::accepted).orElse(null));
    }
}
