package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.GraphEvaluationResult;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.word.Word;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Simulator {

    public Optional<GraphEvaluationResult> simulatedDFA(CompactDFA<String> dfa, String input) {
        if (dfa == null || input == null) return Optional.empty();

        Integer nodeID = dfa.getInitialState();
        if (nodeID == null || nodeID < 0) return Optional.empty();

        boolean accepted = inputAccepted(dfa, input);
        nodeID = calculateSelectedNodes(dfa, input, nodeID);

        return Optional.of(new GraphEvaluationResult(nodeID, accepted));
    }

    private boolean inputAccepted(CompactDFA<String> dfa, String input) {
        Word<String> inputToken = getTokenFromInput(input);
        return dfa.accepts(inputToken);
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
