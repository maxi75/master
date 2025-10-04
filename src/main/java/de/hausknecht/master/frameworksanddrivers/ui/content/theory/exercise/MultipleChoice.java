package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.TheoryPageData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

    public void addMultipleChoiceExercise(TheoryPageData.Exercise exercise, VBox container) {
        addTitle(container);
        addQuestion(exercise, container);

        List<CheckBox> checkBoxes = new ArrayList<>();
        List<String> answers = exercise.getAnswers() != null ? exercise.getAnswers() : List.of();
        answers.forEach(answer -> addAnswer(answer, container, checkBoxes));
        addCheck(exercise, checkBoxes, container);
    }

    private void addTitle(VBox container) {
        Label title = new Label(CONTAINER_NAME);
        title.getStyleClass().add("exerciseTitle");
        title.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().add(title);
    }

    private void addQuestion(TheoryPageData.Exercise exercise, VBox container) {
        Text question = new Text("Frage: " + (exercise.getQuestion() != null ? exercise.getQuestion() : "Error loading question"));
        question.getStyleClass().add("exerciseQuestion");

        TextFlow textFlow = createTextFlow(question, container);
        textFlow.getStyleClass().add("exerciseQuestionContainer");
        container.getChildren().add(textFlow);
    }

    private void addAnswer(String answer, VBox container, List<CheckBox> boxes) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hboxAnswerContainer");
        hBox.setSpacing(25);

        CheckBox checkBox = new CheckBox();
        hBox.getChildren().add(checkBox);
        boxes.add(checkBox);

        Text answerText = new Text(answer);
        answerText.getStyleClass().add("exerciseMultipleChoiceAnswer");
        TextFlow textFlow = createTextFlow(answerText, container);

        hBox.getChildren().add(textFlow);
        container.getChildren().add(hBox);
    }

    private TextFlow createTextFlow(Text text, VBox container) {
        TextFlow  textFlow = new TextFlow(text);
        textFlow.setMaxWidth(Double.MAX_VALUE);
        textFlow.prefWidthProperty().bind(container.widthProperty());
        return textFlow;
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
                .forEach(i -> {
                    checkBoxes.get(i).getStyleClass().remove("multipleChoiceWrongAnswer");
                    checkBoxes.get(i).getStyleClass().remove("multipleChoiceCorrectAnswer");
                    checkBoxes.get(i).setSelected(exercise.getCorrectAnswers().contains(i));
                }));

        return button;
    }

    private Button addCheckButton(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes) {
        Button button = new Button("Prüfe Lösung");
        button.getStyleClass().add("buttonCheck");

        button.setOnAction(_ -> IntStream.range(0, checkBoxes.size())
                .forEach(i -> {
                    checkBoxes.get(i).getStyleClass().removeAll();
                    boolean isSelected = checkBoxes.get(i).isSelected();
                    boolean shouldBeSelected = exercise.getCorrectAnswers().contains(i);

                    if (isSelected != shouldBeSelected) checkBoxes.get(i).getStyleClass().add("multipleChoiceWrongAnswer");
                    if (isSelected == shouldBeSelected) checkBoxes.get(i).getStyleClass().add("multipleChoiceCorrectAnswer");
                }));

        return button;
    }
}
