package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.TheoryPageData;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class MultipleChoice {

    private static final String CONTAINER_NAME = "Multiple Choice";

    public void addMultipleChoiceExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, exercise.getQuestion() != null ? exercise.getQuestion() : "Error loading question");

        List<CheckBox> checkBoxes = new ArrayList<>();
        List<String> answers = exercise.getAnswers() != null ? exercise.getAnswers() : List.of();
        answers.forEach(answer -> addAnswer(answer, container, checkBoxes, exerciseContainer));
        addCheck(exercise, checkBoxes, container);
    }

    private void addAnswer(String answer, VBox container, List<CheckBox> boxes, ExerciseContainer exerciseContainer) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxAnswerContainer");
        hBox.setSpacing(25);

        CheckBox checkBox = new CheckBox();
        hBox.getChildren().add(checkBox);
        boxes.add(checkBox);

        Text answerText = new Text(answer);
        answerText.getStyleClass().add("exerciseMultipleChoiceAnswer");
        TextFlow textFlow = exerciseContainer.createTextFlow(answerText, container);

        hBox.getChildren().add(textFlow);
        container.getChildren().add(hBox);
    }

    private void addCheck(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxCheck");
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().add(addSolveButton(exercise, checkBoxes));
        hBox.getChildren().add(addCheckButton(exercise, checkBoxes));

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes) {
        Button button = new Button("Lösen");
        button.getStyleClass().add("buttonSolve");

        button.setOnAction(_ -> IntStream.range(0, checkBoxes.size())
                .forEach(i -> Platform.runLater(() -> {
                    checkBoxes.get(i).getStyleClass().removeAll("multipleChoiceWrongAnswer", "multipleChoiceCorrectAnswer");
                    checkBoxes.get(i).setSelected(exercise.getCorrectAnswers().contains(i));
                })));

        return button;
    }

    private Button addCheckButton(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes) {
        Button button = new Button("Prüfe Lösung");
        button.getStyleClass().add("buttonCheck");

        button.setOnAction(_ -> IntStream.range(0, checkBoxes.size())
                .forEach(i -> Platform.runLater(() -> {
                    checkBoxes.get(i).getStyleClass().removeAll("multipleChoiceWrongAnswer", "multipleChoiceCorrectAnswer");
                    boolean isSelected = checkBoxes.get(i).isSelected();
                    boolean shouldBeSelected = exercise.getCorrectAnswers().contains(i);

                    checkBoxes.get(i).getStyleClass().add(isSelected != shouldBeSelected ?
                                    "multipleChoiceWrongAnswer" :
                                    "multipleChoiceCorrectAnswer");
                })));

        return button;
    }
}
