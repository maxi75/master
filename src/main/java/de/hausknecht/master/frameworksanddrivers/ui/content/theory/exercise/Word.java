package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.domain.content.ExcerciseType;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.usecase.DataAccessor;
import de.hausknecht.master.usecase.GraphAdministrator;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static de.hausknecht.master.ConstantProvider.*;
import static de.hausknecht.master.entity.domain.automata.AutomataSimulation.DFA;
import static de.hausknecht.master.entity.domain.automata.AutomataSimulation.NFA;

@Component
@RequiredArgsConstructor
public class Word {
    static final String CONTAINER_NAME = "Eingabewort finden";
    static final String QUESTION = "Welche beispielhafte Eingabefolge wird von dem Automaten akzeptiert? \n";
    static final String INPUT_PROMPT = "Eingabewort";
    static final String INPUT_CSS_STYLE = "textFieldAnswer";
    static final String INPUT_CONTAINER_CSS_STYLE = "hboxAnswerContainer";

    private final DataAccessor dataAccessor;
    private final GraphAdministrator graphAdministrator;
    private final PointSystemAdministrator pointSystemAdministrator;

    public void addWordExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, buildQuestion(data));
        TextField textField = addInput(container);

        addCheck(data, textField, exercise.getKind().equals(ExcerciseType.DEA_WORD) ? DFA : NFA, container);
    }

    private String buildQuestion(GraphData data) {
        return buildQuestionIntroduction(data) + buildTransitionDefinition(data) + QUESTION;
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
        data.transitions().forEach(transition -> computeSingleTransitionDefinition(transition, byTarget));

        StringBuilder definitionBuilder = new StringBuilder();
        byTarget.forEach((target, definitions) ->
                definitionBuilder.append(computeTransitionDefinitionByTarget(target, definitions)));

        return String.format(DEFINITION, definitionBuilder);
    }
    private void computeSingleTransitionDefinition(TransitionTriple transition, Map<String, Set<String>> byTarget) {
        if (transition == null) return;

        String term = String.format(TERM, transition.fromNode(), transition.transitionWord());
        byTarget.computeIfAbsent(transition.toNode(), _ -> new HashSet<>()).add(term);
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

    private TextField addInput(VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(INPUT_CONTAINER_CSS_STYLE);

        TextField textField = new TextField();
        textField.getStyleClass().add(INPUT_CSS_STYLE);
        textField.setPromptText(INPUT_PROMPT);

        hBox.getChildren().add(textField);
        container.getChildren().add(hBox);
        return textField;
    }

    private void addCheck(GraphData graphData, TextField textField, AutomataSimulation automata, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(CHECK_CSS_STYLE);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().add(addSolveButton(graphData, textField));
        hBox.getChildren().add(addCheckButton(graphData, textField, automata));

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, TextField textField) {
        Button button = new Button(SOLVE);
        button.getStyleClass().add(SOLVE_CSS_ID);

        button.setOnAction(_ -> Platform.runLater(() -> {
            pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_ON_SOLVE);
            textField.getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
            textField.setText(graphAdministrator.findAcceptingInputForGraphData(graphData));
        }));

        return button;
    }

    private Button addCheckButton(GraphData graphData, TextField textField, AutomataSimulation automata) {
        Button button = new Button(CHECK_SOLUTION);
        button.getStyleClass().add(CHECK_SOLUTION_CSS_ID);

        button.setOnAction(_ -> Platform.runLater(() -> {
            textField.getStyleClass().removeAll(WRONG_ANSWER_CSS_ID,  CORRECT_ANSWER_CSS_ID);
            boolean result = graphAdministrator.isInputAccepted(graphData, textField.getText(), automata);

            textField.getStyleClass().add(result ? CORRECT_ANSWER_CSS_ID : WRONG_ANSWER_CSS_ID);
            if(result) pointSystemAdministrator.addPoints(ADD_POINTS_CORRECT_CHECK);
            pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_WRONG_CHECK);
        }));

        return button;
    }
}
