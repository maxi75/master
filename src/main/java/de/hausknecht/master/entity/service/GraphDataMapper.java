package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.TheoryPageData;
import de.hausknecht.master.entity.domain.TransitionTriple;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GraphDataMapper {
    private static final String TRANSITION_MID = "--\\s*\\[?(.*?)]?\\s*-->";
    private static final Pattern TRANSITION = Pattern.compile("^\\s*(\\S+)\\s*" + TRANSITION_MID + "\\s*(\\S+)\\s*$");

    public GraphData mapTheoryPageDataExerciseToGraphData(TheoryPageData.Exercise exercise) {
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
