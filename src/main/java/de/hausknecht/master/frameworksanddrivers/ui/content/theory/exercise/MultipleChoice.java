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

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static de.hausknecht.master.ConstantProvider.*;

@Component
@AllArgsConstructor
public class MultipleChoice {

    static final String CONTAINER_NAME = "Multiple Choice";
    static final String ERROR_WHILE_LOADING_QUESTION = "Error while loading question.";
    static final String ANSWER_CSS_STYLE = "exerciseMultipleChoiceAnswer";
    static final String ANSWER_CONTAINER_CSS_STYLE = "hboxAnswerContainer";
    static final String WRONG_ANSWER_CSS_ID = "multipleChoiceWrongAnswer";
    static final String CORRECT_ANSWER_CSS_ID = "multipleChoiceCorrectAnswer";

    private final PointSystemAdministrator pointSystemAdministrator;

    public void addMultipleChoiceExercise(TheoryPageData.Exercise exercise, VBox container, ExerciseContainer exerciseContainer) {
        exerciseContainer.addTitle(container, CONTAINER_NAME);
        exerciseContainer.addQuestion(container, exercise.getQuestion() != null ? exercise.getQuestion() : ERROR_WHILE_LOADING_QUESTION);

        List<CheckBox> checkBoxes = new ArrayList<>();
        List<String> answers = exercise.getAnswers() != null ? exercise.getAnswers() : List.of();
        answers.forEach(answer -> addAnswer(answer, container, checkBoxes, exerciseContainer));
        addCheck(exercise, checkBoxes, container);
    }

    private void addAnswer(String answer, VBox container, List<CheckBox> boxes, ExerciseContainer exerciseContainer) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(ANSWER_CONTAINER_CSS_STYLE);
        hBox.setSpacing(25);

        CheckBox checkBox = new CheckBox();
        hBox.getChildren().add(checkBox);
        boxes.add(checkBox);

        Text answerText = new Text(answer);
        answerText.getStyleClass().add(ANSWER_CSS_STYLE);
        TextFlow textFlow = exerciseContainer.createTextFlow(answerText, container);

        hBox.getChildren().add(textFlow);
        container.getChildren().add(hBox);
    }

    private void addCheck(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes, VBox container) {
        HBox hBox = new HBox();
        hBox.getStyleClass().add(CHECK_CSS_STYLE);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().add(addSolveButton(exercise, checkBoxes));
        hBox.getChildren().add(addCheckButton(exercise, checkBoxes));

        container.getChildren().add(hBox);
    }

    private Button addSolveButton(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes) {
        Button button = new Button(SOLVE);
        button.getStyleClass().add(SOLVE_CSS_ID);

        button.setOnAction(ignored -> {
            IntStream.range(0, checkBoxes.size())
                .forEach(i -> Platform.runLater(() -> {
                    checkBoxes.get(i).getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
                    checkBoxes.get(i).setSelected(exercise.getCorrectAnswers().contains(i));
                }));
            pointSystemAdministrator.subtractPoints(SUBTRACT_POINTS_ON_SOLVE);
        });

        return button;
    }

    private Button addCheckButton(TheoryPageData.Exercise exercise, List<CheckBox> checkBoxes) {
        Button button = new Button(CHECK_SOLUTION);
        button.getStyleClass().add(CHECK_SOLUTION_CSS_ID);

        button.setOnAction(ignored -> {
            int points = 0;
            for (int i = 0; i < checkBoxes.size(); i++) {
                checkBoxes.get(i).getStyleClass().removeAll(WRONG_ANSWER_CSS_ID, CORRECT_ANSWER_CSS_ID);
                boolean isSelected = checkBoxes.get(i).isSelected();
                boolean shouldBeSelected = exercise.getCorrectAnswers().contains(i);

                checkBoxes.get(i).getStyleClass().add(isSelected != shouldBeSelected ?
                        WRONG_ANSWER_CSS_ID :
                        CORRECT_ANSWER_CSS_ID);

                points = isSelected != shouldBeSelected ? points - MULTIPLE_CHOICE_WRONG_ANSWER_POINTS : points + MULTIPLE_CHOICE_CORRECT_ANSWER_POINTS;
            }

            if (points >= 0) pointSystemAdministrator.addPoints(points);
            else pointSystemAdministrator.subtractPoints((points * -1));
        });

        return button;
    }
}
