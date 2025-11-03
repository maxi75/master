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

import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GraphDataMapper {
    private static final String FROM_NODE = "^\\s*(\\S+)\\s*";
    private static final String TRANSITION_WORD = "--\\s*\\[?(.*?)]?\\s*-->";
    private static final String TO_NODE = "\\s*(\\S+)\\s*$";
    private static final Pattern TRANSITION = Pattern.compile(FROM_NODE + TRANSITION_WORD + TO_NODE);

    public GraphData mapTheoryPageDataExerciseToGraphData(TheoryPageData.Exercise exercise) {
        if (exercise == null) return null;

        return new GraphData(
                extractAvailableNodes(exercise),
                getStartingNode(exercise),
                extractEndingNodes(exercise),
                extractTransitionTriple(exercise));
    }

    private List<String> extractAvailableNodes(TheoryPageData.Exercise exercise) {
        return exercise.getNodes() != null ? exercise.getNodes() : null;
    }

    private String getStartingNode(TheoryPageData.Exercise exercise) {
        return exercise.getStartingNode() != null ? exercise.getStartingNode() : null;
    }

    private List<String> extractEndingNodes(TheoryPageData.Exercise exercise) {
        return exercise.getEndingNodes() != null ? exercise.getEndingNodes() : null;
    }

    private List<TransitionTriple> extractTransitionTriple(TheoryPageData.Exercise exercise) {
        if (exercise.getTransitions() == null) return null;

        List<TransitionTriple> transitionTriples = new ArrayList<>();
        exercise.getTransitions().forEach(transition -> computeSingleTransitionTriple(transition, transitionTriples));
        return transitionTriples;
    }

    private void computeSingleTransitionTriple(String transition, List<TransitionTriple> transitionTriples) {
        if (transition == null) return;
        Matcher matcher = TRANSITION.matcher(transition);
        if (!matcher.find()) return;

        String src = matcher.group(1).trim();
        String transitionWord = matcher.group(2).trim();
        String targetWord = matcher.group(3).trim();

        transitionTriples.add(new TransitionTriple(src, targetWord, transitionWord));
    }
}
