package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.DfaValues;
import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.GraphEvaluationResult;
import de.hausknecht.master.entity.domain.NfaValues;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class GraphRendering {

    private final DotWriter dotWriter;
    private final DFAGenerator dfaGenerator;
    private final Simulator simulator;

    public Optional<String> dfaToDot(GraphData graphData) {
        return simulatedDFAToDot(graphData, null);
    }

    public Optional<String> simulatedDFAToDot(GraphData graphData, String input) {
        DfaValues dfaValues = dfaGenerator.generateCompactDFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedDFA(dfaValues.dfa(), input);
        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.of(dotWriter.toDot(dfaValues, null, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<String> nfaToDot(GraphData graphData) {
        return simulatedNFAToDot(graphData, null);
    }

    public Optional<String> simulatedNFAToDot(GraphData graphData, String input) {
        NfaValues nfaValues = dfaGenerator.generateCompactNFA(graphData);
        Optional<GraphEvaluationResult> simulationResult = simulator.simulatedNFA(nfaValues.nfa(), input);

        Set<Integer> highlightState = simulationResult.map(GraphEvaluationResult::nodeID).orElse(null);
        Boolean accepted = simulationResult.map(GraphEvaluationResult::accepted).orElse(null);

        try {
            return Optional.of(dotWriter.toDot(null, nfaValues, highlightState, accepted));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
