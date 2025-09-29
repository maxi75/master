package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.GraphEvaluationResult;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import net.automatalib.word.Word;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Simulator {

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
}
