package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.*;
import de.hausknecht.master.interfaceadapters.DataAccessor;
import de.hausknecht.master.interfaceadapters.GraphAdministrator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static de.hausknecht.master.entity.domain.AutomataSimulation.DFA;
import static de.hausknecht.master.entity.domain.AutomataSimulation.NFA;

@Component
@RequiredArgsConstructor
public class Word {
    private static final String CONTAINER_NAME = "Eingabewort finden";

    private final DataAccessor dataAccessor;
    private final GraphAdministrator graphAdministrator;

    public void addWordExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, buildQuestion(data));
        TextField textField = addInput(container);

        addCheck(data, textField, exercise.getKind().equals(ExcerciseType.DEA_WORD) ? DFA : NFA, container);
    }

    private String buildQuestion(GraphData data) {
        return buildQuestionIntroduction(data) + buildTransitionDefinition(data) + buildQuestionPostfix();
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

    private String buildQuestionPostfix() {
        return "Welche beispielhafte Eingabefolge wird von dem Automaten akzeptiert? \n";
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

    private TextField addInput(VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxAnswerContainer");

        TextField textField = new TextField();
        textField.getStyleClass().add("textFieldAnswer");
        textField.setPromptText("Eingabewort");

        hBox.getChildren().add(textField);
        container.getChildren().add(hBox);
        return textField;
    }

    private void addCheck(GraphData graphData, TextField textField, AutomataSimulation automata, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxCheck");
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().add(addSolveButton(graphData, textField));
        hBox.getChildren().add(addCheckButton(graphData, textField, automata));

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, TextField textField) {
        Button button = new Button("Lösen");
        button.getStyleClass().add("buttonSolve");

        button.setOnAction(_ -> Platform.runLater(() -> {
            textField.getStyleClass().removeAll("wrongAnswer", "correctAnswer");
            textField.setText(graphAdministrator.findAcceptingInputForGraphData(graphData));
        }));

        return button;
    }

    private Button addCheckButton(GraphData graphData, TextField textField, AutomataSimulation automata) {
        Button button = new Button("Prüfe Lösung");
        button.getStyleClass().add("buttonCheck");

        button.setOnAction(_ -> Platform.runLater(() -> {
            textField.getStyleClass().removeAll("wrongAnswer",  "correctAnswer");
            boolean result = graphAdministrator.isInputAccepted(graphData, textField.getText(), automata);
            textField.getStyleClass().add(result ? "correctAnswer" : "wrongAnswer");
        }));

        return button;
    }
}
