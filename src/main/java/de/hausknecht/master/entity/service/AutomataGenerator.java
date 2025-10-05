package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.DfaValues;
import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.NfaValues;
import de.hausknecht.master.entity.domain.TransitionTriple;
import net.automatalib.alphabet.Alphabet;
import net.automatalib.alphabet.impl.Alphabets;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AutomataGenerator {

    public DfaValues generateCompactDFA(GraphData graphData) {
        Alphabet<String> alphabet = extractAlphabet(graphData);
        CompactDFA<String> dfa = new CompactDFA<>(alphabet);
        DfaValues dfaValues = addNodes(graphData, dfa);
        addTransitions(dfaValues, graphData);
        return dfaValues;
    }

    public NfaValues generateCompactNFA(GraphData graphData) {
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

    private DfaValues addNodes(GraphData graphData, CompactDFA<String> dfa) {
        Map<String, Integer> nodeToID = new LinkedHashMap<>();
        Map<Integer, String> idToNode = new LinkedHashMap<>();
        for (String node : graphData.availableNodes()) {
            int id = graphData.startingNode() != null && graphData.startingNode().equals(node) ?
                    dfa.addIntInitialState() : dfa.addState();
            dfa.setAccepting(id, graphData.endingNodes().contains(node));
            nodeToID.put(node, id);
            idToNode.put(id, node);
        }
        return new DfaValues(dfa, idToNode, nodeToID);
    }

    private NfaValues addNodes(GraphData graphData, CompactNFA<String> nfa) {
        Map<String, Integer> nodeToID = new LinkedHashMap<>();
        Map<Integer, String> idToNode = new LinkedHashMap<>();
        for (String node : graphData.availableNodes()) {
            int id = graphData.startingNode() != null && graphData.startingNode().equals(node) ?
                    nfa.addInitialState() : nfa.addState();
            nfa.setAccepting(id, graphData.endingNodes().contains(node));
            nodeToID.put(node, id);
            idToNode.put(id, node);
        }
        return new NfaValues(nfa, idToNode, nodeToID);
    }

    private void addTransitions(DfaValues dfaValues, GraphData graphData) {
        for (TransitionTriple transition : graphData.transitions()) {
            Integer fromNode = dfaValues.nodeToId().get(transition.fromNode());
            Integer toNode = dfaValues.nodeToId().get(transition.toNode());
            if (fromNode != null && toNode != null) {
                dfaValues.dfa().setTransition(fromNode, transition.transitionWord(), toNode);
            }
        }
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
}
