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

import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
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

import java.util.HashSet;
import java.util.Set;

import static de.hausknecht.master.ConstantProvider.*;

@Component
@RequiredArgsConstructor
public class Solution {
    static final String CONTAINER_NAME = "Automatenkonstruktion";
    static final String ERROR_WHILE_LOADING_QUESTION = "Error while loading question.";

    private final DataAccessor dataAccessor;
    private final NodeContainer nodeContainer;
    private final NodeDefinitionContainer nodeDefinitionContainer;
    private final TransitionContainer transitionContainer;
    private final GraphAdministrator graphAdministrator;
    private final PointSystemAdministrator pointSystemAdministrator;

    public void addExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);
        if (data == null) return;

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, exercise.getQuestion() != null ? exercise.getQuestion() : ERROR_WHILE_LOADING_QUESTION);

        addCheck(data, container);
    }

    private void addCheck(GraphData graphData, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(CHECK_CSS_STYLE);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        Button checkButton = addCheckButton(graphData);

        hBox.getChildren().add(addSolveButton(graphData, checkButton));
        hBox.getChildren().add(checkButton);

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, Button checkButton) {
        Button button = new Button(SOLVE);
        button.getStyleClass().add(SOLVE_CSS_ID);

        button.setOnAction(ignored -> {
            nodeContainer.deleteAll();
            pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_ON_SOLVE);

            Platform.runLater(() -> {
                checkButton.getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
                graphData.availableNodes().forEach(nodeContainer::addListItem);
                if (graphData.endingNodes() != null) graphData.endingNodes().forEach(nodeDefinitionContainer::addListItem);
                nodeDefinitionContainer.setStartingNode(graphData.startingNode());
                graphData.transitions().forEach(transitionContainer::addListItem);
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

            button.getStyleClass().add(answerIsCorrect ? CORRECT_ANSWER_CSS_ID :  WRONG_ANSWER_CSS_ID);
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
