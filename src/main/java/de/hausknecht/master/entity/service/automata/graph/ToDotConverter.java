package de.hausknecht.master.entity.service.automata.graph;

import de.hausknecht.master.entity.domain.automata.DfaValues;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import net.automatalib.automaton.graph.TransitionEdge;
import net.automatalib.serialization.dot.DefaultDOTVisualizationHelper;
import net.automatalib.serialization.dot.GraphDOT;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

@Service
public class ToDotConverter {

    public String toDot(DfaValues dfaValues, NfaValues nfaValues, Set<Integer> highlightStates, Boolean accepted) throws IOException {
        StringWriter stringWriter = new StringWriter();
        DefaultDOTVisualizationHelper<Integer, TransitionEdge<String, Integer>> helper = new DefaultDOTVisualizationHelper<>() {
            @Override
            public void writePreamble(Appendable appendable) throws IOException {
                super.writePreamble(appendable);
                appendable.append("  rankdir=LR;\n");
                appendable.append("  labelloc=\"t\";\n");
                appendable.append("  fontsize=20;\n");
                appendable.append("  graph [bgcolor=\"transparent\"];\n");
                appendable.append("  node  [fontname=\"Arial\"];\n");
                appendable.append("  edge  [fontname=\"Arial\"];\n");
            }

            @Override
            public void getGlobalNodeProperties(Map<String, String> properties) {
                properties.put("fontname", "Arial");
            }

            @Override
            public void getGlobalEdgeProperties(Map<String, String> properties) {
                properties.put("fontname", "Arial");
            }

            @Override
            public boolean getNodeProperties(Integer node, Map<String, String> properties) {
                setUpNodeShape(dfaValues, nfaValues, node, properties);
                setUpNodeLabel(dfaValues, nfaValues, node, properties);
                setUpSimulatedNodeStyle(highlightStates, accepted, node, properties);
                return true;
            }
        };

        if (dfaValues != null) GraphDOT.write(dfaValues.dfa(), dfaValues.dfa().getInputAlphabet(), stringWriter, helper);
        if (nfaValues != null) GraphDOT.write(nfaValues.nfa(), nfaValues.nfa().getInputAlphabet(), stringWriter, helper);
        return stringWriter.toString();
    }

    private void setUpNodeShape(DfaValues dfaValues, NfaValues nfaValues, Integer node, Map<String, String> properties) {
        if (dfaValues != null && node != null) {
            if (dfaValues.dfa().isAccepting(node)) properties.put("shape", "doublecircle");
            else properties.put("shape", "circle");
        }

        if (nfaValues != null && node != null) {
            if (nfaValues.nfa().isAccepting(node)) properties.put("shape", "doublecircle");
            else properties.put("shape", "circle");
        }
    }

    private void setUpNodeLabel(DfaValues dfaValues, NfaValues nfaValues, Integer node, Map<String, String> properties) {
        if (dfaValues != null && node != null) {
            String name = dfaValues.idToNode().get(node);
            if (name != null) properties.put("label", name);
        }

        if (nfaValues != null && node != null) {
            String name = nfaValues.idToNode().get(node);
            if (name != null) properties.put("label", name);
        }
    }

    private void setUpSimulatedNodeStyle(Set<Integer> highlightStates, Boolean accepted, Integer node, Map<String, String> properties) {
        if (accepted != null && node != null && highlightStates != null && highlightStates.contains(node)) {
            if (accepted) {
                properties.put("style", "filled");
                properties.put("fillcolor", "#d4edda");
                properties.put("color", "#2e7d32");
                properties.put("fontcolor", "#1b5e20");
                properties.put("penwidth", "2");
            } else {
                properties.put("style", "filled");
                properties.put("fillcolor", "#f8d7da");
                properties.put("color", "#c62828");
                properties.put("fontcolor", "#8e0000");
                properties.put("penwidth", "2");
            }
        }
    }
}
