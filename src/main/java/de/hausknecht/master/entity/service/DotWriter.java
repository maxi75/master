package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.DfaValues;
import net.automatalib.automaton.graph.TransitionEdge;
import net.automatalib.serialization.dot.DefaultDOTVisualizationHelper;
import net.automatalib.serialization.dot.GraphDOT;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
public class DotWriter {

    public String toDot(DfaValues dfaValues, Integer highlightState, Boolean accepted) throws IOException {
        StringWriter stringWriter = new StringWriter();
        DefaultDOTVisualizationHelper<Integer, TransitionEdge<String, Integer>> helper = new DefaultDOTVisualizationHelper<Integer, TransitionEdge<String, Integer>>() {
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
                if (dfaValues.dfa().isAccepting(node)) properties.put("shape", "doublecircle");
                else properties.put("shape", "circle");

                String name = dfaValues.idToNode().get(node);
                if (name != null) properties.put("label", name);

                if (accepted != null && node.equals(highlightState)) {
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
                return true;
            }
        };

        GraphDOT.write(dfaValues.dfa(), dfaValues.dfa().getInputAlphabet(), stringWriter, helper);
        return stringWriter.toString();
    }
}
