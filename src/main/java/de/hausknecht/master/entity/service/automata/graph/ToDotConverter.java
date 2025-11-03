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

import de.hausknecht.master.entity.domain.automata.NfaValues;
import net.automatalib.automaton.fsa.impl.CompactDFA;
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

    public String toDot(CompactDFA<String> dfa, NfaValues nfaValues, Set<Integer> highlightStates, Boolean accepted) throws IOException {
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
                setUpNodeShape(dfa, nfaValues, node, properties);
                setUpNodeLabel(dfa, nfaValues, node, properties);
                setUpSimulatedNodeStyle(highlightStates, accepted, node, properties);
                return true;
            }
        };

        if (dfa != null) GraphDOT.write(dfa, dfa.getInputAlphabet(), stringWriter, helper);
        if (nfaValues != null) GraphDOT.write(nfaValues.nfa(), nfaValues.nfa().getInputAlphabet(), stringWriter, helper);
        return stringWriter.toString();
    }

    private void setUpNodeShape(CompactDFA<String> dfa, NfaValues nfaValues, Integer node, Map<String, String> properties) {
        if (dfa != null && node != null) {
            if (dfa.isAccepting(node)) properties.put("shape", "doublecircle");
            else properties.put("shape", "circle");
        }

        if (nfaValues != null && node != null) {
            if (nfaValues.nfa().isAccepting(node)) properties.put("shape", "doublecircle");
            else properties.put("shape", "circle");
        }
    }

    private void setUpNodeLabel(CompactDFA<String> dfa, NfaValues nfaValues, Integer node, Map<String, String> properties) {
       /* if (dfa != null && node != null) {
            String name = dfa.idToNode().get(node);
            if (name != null) properties.put("label", name);
        }*/

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
