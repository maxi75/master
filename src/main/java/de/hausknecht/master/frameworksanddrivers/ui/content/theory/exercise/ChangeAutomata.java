package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

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

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.domain.content.ExerciseType;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.simulationOverlay.SimulationOverlay;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeDefinitionContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.TransitionContainer;
import de.hausknecht.master.usecase.DataAccessor;
import de.hausknecht.master.usecase.GraphAdministrator;
import de.hausknecht.master.usecase.PointSystemAdministrator;
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

import static de.hausknecht.master.ConstantProvider.*;

@Component
@RequiredArgsConstructor
public class ChangeAutomata {
    static final String CONTAINER_NAME = "DEA <-> NEA";
    static final String DEA = "DEA";
    static final String NEA = "NEA";
    static final String QUESTION = "Baue zu dem gegebenen %s einen beispielhaften %s in der Simulationsansicht. \n";

    private final DataAccessor dataAccessor;
    private final NodeContainer nodeContainer;
    private final NodeDefinitionContainer nodeDefinitionContainer;
    private final TransitionContainer transitionContainer;
    private final SimulationOverlay simulationOverlay;
    private final GraphAdministrator graphAdministrator;
    private final PointSystemAdministrator pointSystemAdministrator;

    public void addExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);
        if (data == null) return;

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, buildQuestion(data, exercise));

        addCheck(data, exercise.getKind().equals(ExerciseType.DEA_NEA) ? AutomataSimulation.DEA : AutomataSimulation.NEA, container);
    }

    private String buildQuestion(GraphData data, TheoryPageData.Exercise exercise) {
        return buildQuestionIntroduction(data) + buildTransitionDefinition(data) + buildQuestionPostfix(exercise);
    }

    private String buildQuestionIntroduction(GraphData data) {
        String start = data.getStartingNodeAsString();
        String end = data.getEndingNodeAsString();
        String nodes = data.getNodesAsString();
        String alphabet = data.getAlphabetAsString();

        return String.format(INTRODUCTION, alphabet, nodes, start, end);
    }

    private String buildTransitionDefinition(GraphData data) {
        Map<String, Set<String>> byTarget = new HashMap<>();
        if (data.transitions() == null) return "";
        data.transitions().forEach(transition -> computeSingleTransitionDefinition(transition, byTarget));

        StringBuilder definitionBuilder = new StringBuilder();
        byTarget.forEach((target, definitions) ->
                definitionBuilder.append(computeTransitionDefinitionByTarget(target, definitions)));

        return String.format(DEFINITION, definitionBuilder);
    }

    private void computeSingleTransitionDefinition(TransitionTriple transition, Map<String, Set<String>> byTarget) {
        if (transition == null) return;

        String term = String.format(TERM, transition.fromNode(), transition.transitionWord());
        byTarget.computeIfAbsent(transition.toNode(), ignored -> new HashSet<>()).add(term);
    }

    private String computeTransitionDefinitionByTarget(String target, Set<String> definitions) {
        if (definitions == null || definitions.isEmpty()) return EMPTY_STRING;

        StringBuilder definitionByTarget = new StringBuilder();
        for (String definition : definitions) {
            definitionByTarget.append(definition).append(EQUALS);
        }
        definitionByTarget.append(target).append(SEMICOLON);
        return definitionByTarget.toString();
    }

    private String buildQuestionPostfix(TheoryPageData.Exercise exercise) {
        return String.format(QUESTION,
                exercise.getKind().equals(ExerciseType.DEA_NEA) ? DEA : NEA,
                exercise.getKind().equals(ExerciseType.DEA_NEA) ? NEA : DEA);
    }

    private void addCheck(GraphData graphData, AutomataSimulation automata, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(CHECK_CSS_STYLE);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        Button checkButton = addCheckButton(graphData);

        hBox.getChildren().add(addSolveButton(graphData, automata, checkButton));
        hBox.getChildren().add(checkButton);

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, AutomataSimulation automata, Button checkButton) {
        Button button = new Button(SOLVE);
        button.getStyleClass().add(SOLVE_CSS_ID);

        button.setOnAction(ignored -> {
            nodeContainer.deleteAll();
            pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_ON_SOLVE);

            Platform.runLater(() -> {
                checkButton.getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
                graphData.availableNodes().forEach(nodeContainer::addListItem);
                graphData.endingNodes().forEach(nodeDefinitionContainer::addListItem);
                nodeDefinitionContainer.setStartingNode(graphData.startingNode());
                graphData.transitions().forEach(transitionContainer::addListItem);
                simulationOverlay.setComboBox(automata == AutomataSimulation.DEA ? AutomataSimulation.NEA : AutomataSimulation.DEA);
            });
        });
        return button;
    }

    private Button addCheckButton(GraphData graphData) {
        Button button = new Button(CHECK_SOLUTION);
        button.getStyleClass().add(CHECK_SOLUTION_CSS_ID);

        button.setOnAction(ignored -> Platform.runLater(() -> {
            button.getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
            boolean answerIsCorrect = createdDFAIsCorrect(graphData);

            button.getStyleClass().add(answerIsCorrect ? CORRECT_ANSWER_CSS_ID : WRONG_ANSWER_CSS_ID);
            if (answerIsCorrect) pointSystemAdministrator.addPoints(ADD_POINTS_CORRECT_CHECK);
            else pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_WRONG_CHECK);
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
