package de.hausknecht.master.entity.service.automata.graph;

import de.hausknecht.master.entity.domain.automata.DfaValues;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.GraphEvaluationResult;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import de.hausknecht.master.entity.service.automata.definition.AutomataGenerator;
import de.hausknecht.master.entity.service.content.Simulator;
import lombok.AllArgsConstructor;
import net.automatalib.automaton.fsa.impl.CompactDFA;
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
        DfaValues dfaValues = automataGenerator.generateCompactDFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedDFA(dfaValues.dfa(), input);
        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.of(toDotConverter.toDot(dfaValues, null, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public CompactDFA<String> getCompactDFAFromGraphData(GraphData graphData) {
        return automataGenerator.generateCompactDFA(graphData).dfa();
    }

    public boolean acceptsInputDFA(GraphData graphData, String input) {
        DfaValues dfaValues = automataGenerator.generateCompactDFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedDFA(dfaValues.dfa(), input);
        return Boolean.TRUE.equals(simulationResult.map(GraphEvaluationResult::accepted).orElse(null));
    }

    public Optional<String> nfaToDot(GraphData graphData) {
        return simulatedNFAToDot(graphData, null);
    }

    public Optional<String> simulatedNFAToDot(GraphData graphData, String input) {
        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedNFA(nfaValues.nfa(), input);

        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.of(toDotConverter.toDot(null, nfaValues, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean acceptsInputNFA(GraphData graphData, String input) {
        NfaValues nfaValues = automataGenerator.generateCompactNFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedNFA(nfaValues.nfa(), input);
        return Boolean.TRUE.equals(simulationResult.map(GraphEvaluationResult::accepted).orElse(null));
    }
}
