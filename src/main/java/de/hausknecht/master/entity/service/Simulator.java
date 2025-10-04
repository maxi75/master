package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.GraphEvaluationResult;
import lombok.AllArgsConstructor;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import net.automatalib.word.Word;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class Simulator {

    private final AutomataGenerator automataGenerator;

    public Optional<GraphEvaluationResult> simulatedNFA(CompactNFA<String> nfa, String input) {
        if (nfa == null || input == null) return Optional.empty();

        Set<Integer> nodeIDs = nfa.getInitialStates();
        if (nodeIDs == null || nodeIDs.stream().anyMatch(id -> id < 0)) return Optional.empty();

        boolean accepted = inputAccepted(null, nfa, input);
        nodeIDs = calculateSelectedNodes(nfa, input, nodeIDs);

        return Optional.of(new GraphEvaluationResult(nodeIDs, accepted));
    }

    private Set<Integer> calculateSelectedNodes(CompactNFA<String> nfa, String input, Set<Integer> nodeIDs) {
        Set<Integer> calculatedNodes = new LinkedHashSet<>(nodeIDs);
        Word<String> inputToken = getTokenFromInput(input);

        for (String inputWord : inputToken) {
            Set<Integer> nextNodes = new LinkedHashSet<>();

            for (Integer node : calculatedNodes) {
                Set<Integer> successors = nfa.getSuccessors(node, inputWord);
                if (successors != null && !successors.isEmpty())
                    nextNodes.addAll(successors);
            }

            if (nextNodes.isEmpty()) return Set.of();
            calculatedNodes = nextNodes;
        }
        return calculatedNodes;
    }

    public Optional<GraphEvaluationResult> simulatedDFA(CompactDFA<String> dfa, String input) {
        if (dfa == null || input == null) return Optional.empty();

        Integer nodeID = dfa.getInitialState();
        if (nodeID == null || nodeID < 0) return Optional.empty();

        boolean accepted = inputAccepted(dfa, null, input);
        nodeID = calculateSelectedNodes(dfa, input, nodeID);

        return Optional.of(new GraphEvaluationResult(Set.of(nodeID), accepted));
    }

    private boolean inputAccepted(CompactDFA<String> dfa, CompactNFA<String> nfa, String input) {
        Word<String> inputToken = getTokenFromInput(input);
        if (dfa != null) return dfa.accepts(inputToken);
        return nfa.accepts(inputToken);
    }

    private int calculateSelectedNodes(CompactDFA<String> dfa, String input, int nodeID) {
        Word<String> inputToken = getTokenFromInput(input);
        for (String inputWord : inputToken) {
            int successor = dfa.getSuccessor(nodeID, inputWord);
            if (successor < 0) break;
            nodeID = successor;
        }
        return nodeID;
    }

    private Word<String> getTokenFromInput(String input) {
        List<String> inputTokens = input == null || input.isBlank() ?
                List.of() :
                Arrays.asList(input.trim().split("\\s+"));
        return Word.fromList(inputTokens);
    }

    public Optional<String> findAcceptingInputForGraphData(GraphData graphData) {
        CompactDFA<String> dfa = automataGenerator.generateCompactDFA(graphData).dfa();

        Integer current = dfa.getInitialState();
        if (current == null) return Optional.empty();
        if (dfa.isAccepting(current)) return Optional.of("");

        List<Integer> visited = new ArrayList<>();
        visited.add(current);

        return depthFirstSearch(dfa, current, visited);
    }

    private Optional<String> depthFirstSearch(CompactDFA<String> dfa, int current, List<Integer> visited) {
        List<Map<Integer, String>> toVisit = reachedAndNotAlreadyVisitedStates(dfa,  current, visited);

        for (Map<Integer, String> step : toVisit) {
            Map.Entry<Integer, String> entry = step.entrySet().iterator().next();
            int next = entry.getKey();
            String token = entry.getValue();

            if (dfa.isAccepting(next)) return Optional.of(token);

            Optional<String> tail = depthFirstSearch(dfa, next, visited);
            if (tail.isPresent()) return Optional.of(token + tail.get());
        }

        return Optional.empty();
    }

    private List<Map<Integer, String>> reachedAndNotAlreadyVisitedStates(CompactDFA<String> dfa, int current, List<Integer> visited) {
        List<Map<Integer, String>> result = new ArrayList<>();
        for (String token : dfa.getInputAlphabet()) {
            int successor = dfa.getSuccessor(current, token);

            if (successor < 0 || visited.contains(successor)) continue;
            visited.add(successor);
            result.add(Map.of(successor, token));
        }
        return result;
    }
}
