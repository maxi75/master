package de.hausknecht.master.entity.service.automata.definition;

import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import net.automatalib.alphabet.Alphabet;
import net.automatalib.alphabet.impl.Alphabets;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AutomataGenerator {

    public NfaValues generateCompactNFA(GraphData graphData) {
        if (graphData == null || graphData.transitions() == null) return null;

        Alphabet<String> alphabet = extractAlphabet(graphData);
        CompactNFA<String> nfa = new CompactNFA<>(alphabet);
        NfaValues nfaValues = addNodes(graphData, nfa);
        addTransitions(nfaValues, graphData);
        return nfaValues;
    }

    private Alphabet<String> extractAlphabet(GraphData graphData) {
        List<String> alphabetList = graphData.transitions().stream()
                .map(TransitionTriple::transitionWord)
                .distinct()
                .toList();

        return Alphabets.fromList(alphabetList);
    }

    private NfaValues addNodes(GraphData graphData, CompactNFA<String> nfa) {
        Map<String, Integer> nodeToID = new LinkedHashMap<>();
        Map<Integer, String> idToNode = new LinkedHashMap<>();
        for (String node : graphData.availableNodes()) {
            int id = graphData.startingNode() != null && graphData.startingNode().equals(node) ?
                    nfa.addInitialState() : nfa.addState();
            if (graphData.endingNodes() != null) nfa.setAccepting(id, graphData.endingNodes().contains(node));
            nodeToID.put(node, id);
            idToNode.put(id, node);
        }
        return new NfaValues(nfa, idToNode, nodeToID);
    }

    private void addTransitions(NfaValues nfaValues, GraphData graphData) {
        for (TransitionTriple transition : graphData.transitions()) {
            Integer fromNode = nfaValues.nodeToId().get(transition.fromNode());
            Integer toNode = nfaValues.nodeToId().get(transition.toNode());
            if (fromNode != null && toNode != null) {
                nfaValues.nfa().addTransition(fromNode, transition.transitionWord(), toNode);
            }
        }
    }

    public CompactDFA<String> removeSinkNodes(CompactDFA<String> dfa) {
        Set<Integer> sinkNodes = dfa.getStates().stream()
                .filter(state -> isSink(dfa, state))
                .collect(Collectors.toSet());

        CompactDFA<String> dfaWithoutSink = new CompactDFA<>(dfa.getInputAlphabet());
        int[] nodes = addNodes(dfa,  dfaWithoutSink, sinkNodes);
        if (dfa.getInitialState() != null) dfaWithoutSink.setInitialState(nodes[dfa.getInitialState()]);
        addTransitions(dfa, dfaWithoutSink, sinkNodes, nodes);
        return dfaWithoutSink;
    }

    private boolean isSink(CompactDFA<String> dfa, int nodeId) {
        if (dfa.isAccepting(nodeId) || (dfa.getInitialState() != null && dfa.getInitialState() == nodeId)) return false;

        for (String sign : dfa.getInputAlphabet()) {
            int successor = dfa.getSuccessor(nodeId, sign);
            if (successor < 0) return false;
            if (successor != nodeId) return false;
        }
        return true;
    }

    private int[] addNodes(CompactDFA<String> dfa, CompactDFA<String> dfaWithoutSink, Set<Integer> sinkNodes) {
        int[] nodes = new int[dfa.size()];
        Arrays.fill(nodes, -1);
        for (int nodeId = 0; nodeId < dfa.size(); nodeId++) {
            if (sinkNodes.contains(nodeId)) continue;
            nodes[nodeId] = dfaWithoutSink.addState(dfa.isAccepting(nodeId));
        }
        return nodes;
    }

    private void addTransitions(CompactDFA<String> dfa, CompactDFA<String> dfaWithoutSink, Set<Integer> sinkNodes, int[] nodes) {
        for (int nodeId = 0; nodeId < dfa.size(); nodeId++) {
            if (sinkNodes.contains(nodeId)) continue;

            for (String sign : dfa.getInputAlphabet()) {
                int successor = dfa.getSuccessor(nodeId, sign);
                if (successor < 0 || sinkNodes.contains(successor)) continue;
                dfaWithoutSink.addTransition(nodes[nodeId], sign, nodes[successor]);
            }
        }
    }
}
