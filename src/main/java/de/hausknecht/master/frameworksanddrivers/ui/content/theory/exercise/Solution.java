package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.*;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeDefinitionContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.TransitionContainer;
import de.hausknecht.master.interfaceadapters.DataAccessor;
import de.hausknecht.master.interfaceadapters.GraphAdministrator;
import de.hausknecht.master.interfaceadapters.PointSystemAdministrator;
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

@Component
@RequiredArgsConstructor
public class Solution {
    private static final String CONTAINER_NAME = "Automatenkonstruktion";

    private final DataAccessor dataAccessor;
    private final NodeContainer nodeContainer;
    private final NodeDefinitionContainer nodeDefinitionContainer;
    private final TransitionContainer transitionContainer;
    private final GraphAdministrator graphAdministrator;
    private final PointSystemAdministrator pointSystemAdministrator;

    public void addWordExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        GraphData data = dataAccessor.getGraphDataFromTheoryPageDataExercise(exercise);

        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, exercise.getQuestion() != null ? exercise.getQuestion() : "Error while loading question.");

        addCheck(data, container);
    }

    private void addCheck(GraphData graphData, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxCheck");
        hBox.setAlignment(Pos.CENTER_RIGHT);

        Button checkButton = addCheckButton(graphData);

        hBox.getChildren().add(addSolveButton(graphData, checkButton));
        hBox.getChildren().add(checkButton);

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(GraphData graphData, Button checkButton) {
        Button button = new Button("Lösen");
        button.getStyleClass().add("buttonSolve");

        button.setOnAction(_ -> {
            nodeContainer.deleteAll();
            pointSystemAdministrator.subtractPoints(20);

            Platform.runLater(() -> {
                checkButton.getStyleClass().removeAll("wrongAnswer", "correctAnswer");
                graphData.availableNodes().forEach(nodeContainer::addListItem);
                graphData.endingNodes().forEach(nodeDefinitionContainer::addListItem);
                nodeDefinitionContainer.setStartingNode(graphData.startingNode());
                graphData.transitions().forEach(transitionContainer::addListItem);
            });
        });

        return button;
    }

    private Button addCheckButton(GraphData graphData) {
        Button button = new Button("Prüfe Lösung");
        button.getStyleClass().add("buttonCheck");

        button.setOnAction(_ -> Platform.runLater(() -> {
            button.getStyleClass().removeAll("wrongAnswer", "correctAnswer");
            boolean answerIsCorrect = createdDFAIsCorrect(graphData);

            button.getStyleClass().add(answerIsCorrect ? "correctAnswer" :  "wrongAnswer");
            if (answerIsCorrect) pointSystemAdministrator.addPoints(5);
            else pointSystemAdministrator.subtractPoints(10);
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
