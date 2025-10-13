package de.hausknecht.master;

import de.hausknecht.master.entity.domain.automata.*;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import net.automatalib.alphabet.Alphabet;
import net.automatalib.alphabet.impl.Alphabets;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;

import java.util.*;

import static de.hausknecht.master.entity.domain.content.ExerciseType.SOLUTION;

public class TestDataGenerator {

    public static String NODE_0 = "0";
    public static String NODE_1 = "1";
    public static String NODE_2 = "2";
    public static String NODE_3 = "3";

    public static String CHAR_A = "a";
    public static String CHAR_B = "b";
    public static String CHAR_C = "c";

    public static String QUESTION = "A sample question";
    public static List<Integer> CORRECT_ANSWERS = new ArrayList<>(List.of(2, 3));
    public static List<String> ANSWERS = new ArrayList<>(List.of("Answer 1", "Answer 2", "Answer 3"));
    public static List<String> NODES = new ArrayList<>(List.of(NODE_0, NODE_1, NODE_2, NODE_3));
    public static String STARTING_NODE = NODE_0;
    public static List<String> ENDING_NODES = new ArrayList<>(List.of(NODE_2, NODE_3));
    public static List<String> TRANSITIONS = new ArrayList<>(List.of(
            NODE_0 + " --" + CHAR_A + "--> " + NODE_1,
            NODE_1 + " --" + CHAR_B + "--> " + NODE_2,
            NODE_2 + " --" + CHAR_C + "--> " + NODE_3,
            NODE_3 + " --" + CHAR_C + "--> " + NODE_3,
            NODE_3 + " --" + CHAR_C + "--> " + NODE_2
    ));

    private static final String DOT = """
                digraph g {
                	node [fontname="Arial"];
                	edge [fontname="Arial"];
                  rankdir=LR;
                  labelloc="t";
                  fontsize=20;
                  graph [bgcolor="transparent"];
                  node  [fontname="Arial"];
                  edge  [fontname="Arial"];
                
                	s0 [shape="circle" label="0"];
                	s1 [shape="circle" label="1"];
                	%s
                	s0 -> s1 [label="a"];
                	s1 -> s2 [label="b"];
                	s2 -> s3 [label="c"];%s
                	s3 -> s3 [label="c"];
                
                __start0 [label="" shape="none" width="0" height="0"];
                __start0 -> s0;
                
                }
                """;

    private static final String DOT_HIGHLIGHT = "s2 [penwidth=\"2\" fillcolor=\"#d4edda\" shape=\"doublecircle\" color=\"#2e7d32\" fontcolor=\"#1b5e20\" style=\"filled\" label=\"2\"];\n" +
            "\ts3 [penwidth=\"2\" fillcolor=\"#d4edda\" shape=\"doublecircle\" color=\"#2e7d32\" fontcolor=\"#1b5e20\" style=\"filled\" label=\"3\"];";

    private static final String DOT_HIGHLIGHT_IS_NULL= "s2 [shape=\"doublecircle\" label=\"2\"];\n" +
            "\ts3 [shape=\"doublecircle\" label=\"3\"];";

    private static final String DOT_WITHOUT_HIGHLIGHT = "s2 [shape=\"doublecircle\" label=\"2\"];\n" +
            "\ts3 [shape=\"doublecircle\" label=\"3\"];";

    private static final String DOT_NFA_EXTRA_LINE = "\n\ts3 -> s2 [label=\"c\"];";

    private static final String MAIN_HEADING = "MAIN_HEADING";
    private static final String SUB_HEADING = "SUB_HEADING";
    private static final String TEXT = "TEXT";
    private static final String IMAGE_PATH = "icon.png";
    private static final String IMAGE_ALT_TEXT = "IMAGE_ALT_TEXT";
    private static final String IMAGE_SUB_TEXT = "IMAGE_SUB_TEXT";

    public static final String BATCH_TIME_NOVICE_PATH_WITHOUT_SATURATION = "/packaging/batches/time/TimeNoviceWithoutSaturation.png";
    public static final String BATCH_TIME_INTERMEDIATE_PATH_WITHOUT_SATURATION = "/packaging/batches/time/TimeIntermediateWithoutSaturation.png";
    public static final String BATCH_TIME_MASTER_PATH_WITHOUT_SATURATION = "/packaging/batches/time/TimeMasterWithoutSaturation.png";
    public static final String BATCH_TIME_KING_PATH_WITHOUT_SATURATION = "/packaging/batches/time/TimeKingWithoutSaturation.png";
    public static final String BATCH_EXERCISE_NOVICE_PATH_WITHOUT_SATURATION = "/packaging/batches/exercise/ExerciseNoviceWithoutSaturation.png";
    public static final String BATCH_EXERCISE_INTERMEDIATE_PATH_WITHOUT_SATURATION = "/packaging/batches/exercise/ExerciseIntermediateWithoutSaturation.png";
    public static final String BATCH_EXERCISE_MASTER_PATH_WITHOUT_SATURATION = "/packaging/batches/exercise/ExerciseMasterWithoutSaturation.png";
    public static final String BATCH_EXERCISE_KING_PATH_WITHOUT_SATURATION = "/packaging/batches/exercise/ExerciseKingWithoutSaturation.png";


    public static GraphData getCorrectGraphData() {
        return new GraphData(
                getAvailableNodes(),
                getStartingNode(),
                getEndingNodes(),
                getTransitions());
    }

    public static DfaValues getCorrectDfaValues(boolean withAlphabet, boolean withNodes, boolean withInitialState, boolean withTransitions) {
        return new DfaValues(getCorrectDfa(withAlphabet, withNodes, withInitialState, withTransitions), getCorrectIdToNode(), getCorrectNodeToId());
    }

    public static NfaValues getCorrectNfaValues(boolean withAlphabet, boolean withNodes, boolean withInitialState, boolean withTransitions) {
        return new NfaValues(getCorrectNfa(withAlphabet, withNodes, withInitialState, withTransitions), getCorrectIdToNode(), getCorrectNodeToId());
    }

    public static TheoryPageData getCorrectTheoryPageData() {
        TheoryPageData theoryPageData = new TheoryPageData();
        theoryPageData.setMainHeading(MAIN_HEADING);
        theoryPageData.setContent(List.of(getCorrectTheoryPageDataSection()));
        return theoryPageData;
    }

    public static TheoryPageData.Section getCorrectTheoryPageDataSection() {
        TheoryPageData.Section theoryPageDataSection = new TheoryPageData.Section();
        theoryPageDataSection.setHeading(SUB_HEADING);
        theoryPageDataSection.setText(TEXT);
        theoryPageDataSection.setImage(IMAGE_PATH);
        theoryPageDataSection.setImageAltText(IMAGE_ALT_TEXT);
        theoryPageDataSection.setImageSubText(IMAGE_SUB_TEXT);
        theoryPageDataSection.setExercises(List.of(getCorrectExerciseData()));
        return theoryPageDataSection;
    }

    public static TheoryPageData.Exercise getCorrectExerciseData() {
        TheoryPageData.Exercise exercise = new TheoryPageData.Exercise();
        exercise.setKind(SOLUTION);
        exercise.setQuestion(QUESTION);
        exercise.setCorrectAnswers(new ArrayList<>(CORRECT_ANSWERS));
        exercise.setAnswers(new ArrayList<>(ANSWERS));
        exercise.setNodes(new ArrayList<>(NODES));
        exercise.setStartingNode(STARTING_NODE);
        exercise.setEndingNodes(new ArrayList<>(ENDING_NODES));
        exercise.setTransitions(new ArrayList<>(TRANSITIONS));
        return exercise;
    }

    public static Optional<GraphEvaluationResult> getGraphEvaluationResultOptional() {
        return Optional.of(getGraphEvaluationResult());
    }

    public static String getDot(boolean dfa, boolean highlight, boolean highlightIsNull) {
        return String.format(DOT,
                highlight ? DOT_HIGHLIGHT : highlightIsNull ? DOT_HIGHLIGHT_IS_NULL : DOT_WITHOUT_HIGHLIGHT,
                dfa ? "" : DOT_NFA_EXTRA_LINE
        );
    }

    public static List<TransitionTriple> getTransitions() {
        List<TransitionTriple> transitions = new ArrayList<>();
        transitions.add(new TransitionTriple(NODE_0, NODE_1, CHAR_A));
        transitions.add(new TransitionTriple(NODE_1, NODE_2, CHAR_B));
        transitions.add(new TransitionTriple(NODE_2, NODE_3, CHAR_C));
        transitions.add(new TransitionTriple(NODE_3, NODE_3, CHAR_C));
        transitions.add(new TransitionTriple(NODE_3, NODE_2, CHAR_C));
        return transitions;
    }

    public static CompactDFA<String> getCorrectDfa(boolean withAlphabet, boolean withNodes, boolean withInitialState, boolean withTransitions) {
        CompactDFA<String> compactDFA = new CompactDFA<>(withAlphabet ? getCorrectAlphabet() : Alphabets.fromList(List.of()));

        if (withNodes) {
            compactDFA.setAccepting(withInitialState ? compactDFA.addInitialState() : compactDFA.addState(), false);
            compactDFA.setAccepting(compactDFA.addState(), false);
            compactDFA.setAccepting(compactDFA.addState(), true);
            compactDFA.setAccepting(compactDFA.addState(), true);
        }

        if (withNodes && withAlphabet && withTransitions) {
            compactDFA.addTransition(0, CHAR_A, 1);
            compactDFA.addTransition(1, CHAR_B, 2);
            compactDFA.addTransition(2, CHAR_C, 3);
            compactDFA.addTransition(3, CHAR_C, 3);
        }
        return compactDFA;
    }

    public static CompactNFA<String> getCorrectNfa(boolean withAlphabet, boolean withNodes, boolean withInitialState, boolean withTransitions) {
        CompactNFA<String> compactNFA = new CompactNFA<>(withAlphabet ? getCorrectAlphabet() : Alphabets.fromList(List.of()));

        if (withNodes) {
            compactNFA.setAccepting(withInitialState ? compactNFA.addInitialState() : compactNFA.addState(), false);
            compactNFA.setAccepting(compactNFA.addState(), false);
            compactNFA.setAccepting(compactNFA.addState(), true);
            compactNFA.setAccepting(compactNFA.addState(), true);
        }

        if (withNodes && withAlphabet && withTransitions) {
            compactNFA.addTransition(0, CHAR_A, 1);
            compactNFA.addTransition(1, CHAR_B, 2);
            compactNFA.addTransition(2, CHAR_C, 3);
            compactNFA.addTransition(3, CHAR_C, 3);
            compactNFA.addTransition(3, CHAR_C, 2);
        }
        return compactNFA;
    }

    private static GraphEvaluationResult getGraphEvaluationResult() {
        return new GraphEvaluationResult(Set.of(2, 3), true);
    }

    private static Map<Integer, String> getCorrectIdToNode() {
        Map<Integer, String> idToNode = new HashMap<>();
        idToNode.put(0, NODE_0);
        idToNode.put(1, NODE_1);
        idToNode.put(2, NODE_2);
        idToNode.put(3, NODE_3);
        return idToNode;
    }

    private static Map<String, Integer> getCorrectNodeToId() {
        Map<String, Integer> nodeToId = new HashMap<>();
        nodeToId.put(NODE_0, 0);
        nodeToId.put(NODE_1, 1);
        nodeToId.put(NODE_2, 2);
        nodeToId.put(NODE_3, 3);
        return nodeToId;
    }

    private static Alphabet<String> getCorrectAlphabet() {
        List<String> alphabetList = new ArrayList<>();
        alphabetList.add(CHAR_A);
        alphabetList.add(CHAR_B);
        alphabetList.add(CHAR_C);

        return Alphabets.fromList(alphabetList);
    }

    private static List<String> getAvailableNodes() {
        List<String> availableNodes = new ArrayList<>();
        availableNodes.add(getStartingNode());
        availableNodes.add(NODE_1);
        availableNodes.addAll(getEndingNodes());
        return availableNodes;
    }

    private static String getStartingNode() {
        return NODE_0;
    }

    private static List<String> getEndingNodes() {
        List<String> endingNodes = new ArrayList<>();
        endingNodes.add(NODE_2);
        endingNodes.add(NODE_3);
        return endingNodes;
    }
}
