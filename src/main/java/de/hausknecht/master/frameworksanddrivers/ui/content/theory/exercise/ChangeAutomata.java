package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.*;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.simulationOverlay.SimulationOverlay;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeDefinitionContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.TransitionContainer;
import de.hausknecht.master.interfaceadapters.DataAccessor;
import de.hausknecht.master.interfaceadapters.GraphAdministrator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import net.automatalib.alphabet.Alphabet;
import net.automatalib.alphabet.impl.Alphabets;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.util.automaton.Automata;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.hausknecht.master.entity.domain.AutomataSimulation.DFA;
import static de.hausknecht.master.entity.domain.AutomataSimulation.NFA;

@Component
@RequiredArgsConstructor
public class ChangeAutomata {
    private static final String CONTAINER_NAME = "DEA <-> NEA";

    private final DataAccessor dataAccessor;
    private final NodeContainer nodeContainer;
    private final NodeDefinitionContainer nodeDefinitionContainer;
    private final TransitionContainer transitionContainer;
    private final SimulationOverlay simulationOverlay;
    private final GraphAdministrator graphAdministrator;

    public void addWordExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, buildQuestion(data, exercise));

        addCheck(data, exercise.getKind().equals(ExcerciseType.DEA_NEA) ? DFA : NFA, container);
    }

    private String buildQuestion(GraphData data, TheoryPageData.Exercise exercise) {
        return buildQuestionIntroduction(data) + buildTransitionDefinition(data) + buildQuestionPostfix(exercise);
    }

    private String buildQuestionIntroduction(GraphData data) {
        String introduction = "Gegeben ist folgende Automatendefinition: \n A=({%s}, {%s}, δ, %s, {%s}) \n\n";
        String startzustand = data.getStartingNodeAsString();
        String endzustaende = data.getEndingNodeAsString();
        String alleZustaende = data.getNodesAsString();
        String alphabet = data.ermittleAlphabetAsString();

        return String.format(introduction, alphabet, alleZustaende, startzustand, endzustaende);
    }

    private String buildTransitionDefinition(GraphData data) {
        String definition = "mit der Zustandsübergangsfunktion: \n %s \n\n";

        Map<String, Set<String>> byTarget = new HashMap<>();
        data.transitions().forEach(transition -> computeSingleTransitionDefinition(transition, byTarget));

        StringBuilder definitionBuilder = new StringBuilder();
        byTarget.forEach((target, definitions) ->
                definitionBuilder.append(computeTransitionDefinitionByTarget(target, definitions)));

        return String.format(definition, definitionBuilder);
    }

    private String buildQuestionPostfix(TheoryPageData.Exercise exercise) {
        return String.format("Baue zu dem gegebenen %s einen beispielhaften %s in der Simulationsansicht. \n",
                exercise.getKind().equals(ExcerciseType.DEA_NEA) ? "DEA" : "NEA",
                exercise.getKind().equals(ExcerciseType.DEA_NEA) ? "NEA" : "DEA");
    }

    private void computeSingleTransitionDefinition(TransitionTriple transition, Map<String, Set<String>> byTarget) {
        if (transition == null) return;

        String term = "δ(" + transition.fromNode() + "," + transition.transitionWord() + ")";
        byTarget.computeIfAbsent(transition.toNode(), _ -> new HashSet<>()).add(term);
    }

    private String computeTransitionDefinitionByTarget(String target, Set<String> definitions) {
        if (definitions == null || definitions.isEmpty()) return "";

        StringBuilder definitionByTarget = new StringBuilder();
        for (String definition : definitions) {
            definitionByTarget.append(definition).append(" = ");
        }
        definitionByTarget.append(target).append(";   ");
        return definitionByTarget.toString();
    }

    private void addCheck(GraphData graphData, AutomataSimulation automata, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxCheck");
        hBox.setAlignment(Pos.CENTER_RIGHT);

        Button checkButton = addCheckButton(graphData);

        hBox.getChildren().add(addSolveButton(graphData, automata, checkButton));
        hBox.getChildren().add(checkButton);

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, AutomataSimulation automata, Button checkButton) {
        Button button = new Button("Lösen");
        button.getStyleClass().add("buttonSolve");

        button.setOnAction(_ -> {
            nodeContainer.deleteAll();

            Platform.runLater(() -> {
                checkButton.getStyleClass().removeAll("wrongAnswer", "correctAnswer");
                graphData.availableNodes().forEach(nodeContainer::addListItem);
                graphData.endingNodes().forEach(nodeDefinitionContainer::addListItem);
                nodeDefinitionContainer.setStartingNode(graphData.startingNode());
                graphData.transitions().forEach(transitionContainer::addListItem);
                simulationOverlay.setComboBox(automata == DFA ? NFA : DFA);
            });
        });
        return button;
    }

    private Button addCheckButton(GraphData graphData) {
        Button button = new Button("Prüfe Lösung");
        button.getStyleClass().add("buttonCheck");

        button.setOnAction(_ -> Platform.runLater(() -> {
            button.getStyleClass().removeAll("wrongAnswer", "correctAnswer");
            button.getStyleClass().add(createdDFAIsCorrect(graphData) ? "correctAnswer" : "wrongAnswer");
        }));

        return button;
    }

    private boolean createdDFAIsCorrect(GraphData graphData) {
        CompactDFA<String> createdDFA = graphAdministrator.getCompactDFAFromCurrentSimulation();
        CompactDFA<String> expectedDFA = graphAdministrator.getCompactDFAFromGraphData(graphData);

        Alphabet<String> unified = unifyAlphabet(createdDFA.getInputAlphabet(),  expectedDFA.getInputAlphabet());
        try {
            return Automata.findSeparatingWord(createdDFA, expectedDFA, unified) == null;
        } catch (Exception e) {
            return false;
        }
    }

    private Alphabet<String> unifyAlphabet(Alphabet<String> alphabet1,  Alphabet<String> alphabet2) {
        Set<String> tokens = new HashSet<>(alphabet1);
        tokens.addAll(alphabet2);
        return Alphabets.fromCollection(tokens);
    }
}
