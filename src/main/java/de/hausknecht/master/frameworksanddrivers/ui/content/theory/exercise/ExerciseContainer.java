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
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseContainer {
    static final String EXERCISE_BOX_CSS_STYLE = "exerciseBox";
    static final String EXERCISE_TITLE_CSS_STYLE = "exerciseTitle";
    static final String EXERCISE_QUESTION_CSS_STYLE = "exerciseQuestion";
    static final String EXERCISE_QUESTION_CONTAINER_CSS_STYLE = "exerciseQuestionContainer";
    static final String EXERCISE_QUESTION = "Frage: ";

    private final MultipleChoice multipleChoice;
    private final Word word;
    private final ChangeAutomata changeAutomata;
    private final Solution solution;

    public void addExercise(TheoryPageData.Exercise exercise, VBox theoryContainer) {
        if (exercise == null || exercise.getKind() == null || theoryContainer == null) return;

        buildContainer(exercise, theoryContainer);
    }

    private void buildContainer(TheoryPageData.Exercise exercise, VBox theoryContainer) {
        VBox container = new VBox();
        container.getChildren().add(buildExerciseBox(exercise));
        theoryContainer.getChildren().add(container);
    }

    private VBox buildExerciseBox(TheoryPageData.Exercise exercise) {
        VBox exerciseBox = new VBox();
        exerciseBox.getStyleClass().add(EXERCISE_BOX_CSS_STYLE);
        addExerciseToContainer(exercise,  exerciseBox);
        return exerciseBox;
    }

    private void addExerciseToContainer(TheoryPageData.Exercise exercise, VBox container) {
        switch (exercise.getKind()) {
            case MULTIPLE_CHOICE -> multipleChoice.addMultipleChoiceExercise(exercise, container, this);
            case WORD -> word.addWordExercise(exercise, container, this);
            case DEA_NEA, NEA_DEA -> changeAutomata.addExercise(exercise, container, this);
            case SOLUTION -> solution.addExercise(exercise, container, this);
        }
    }

    public void addTitle(VBox container, String name) {
        Label title = new Label(name);
        title.getStyleClass().add(EXERCISE_TITLE_CSS_STYLE);
        title.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().add(title);
    }

    public void addQuestion(VBox container, String question) {
        Text questionText = new Text(EXERCISE_QUESTION + question);
        questionText.getStyleClass().add(EXERCISE_QUESTION_CSS_STYLE);

        TextFlow textFlow = createTextFlow(questionText, container);
        textFlow.getStyleClass().add(EXERCISE_QUESTION_CONTAINER_CSS_STYLE);
        container.getChildren().add(textFlow);
    }

    public TextFlow createTextFlow(Text text, VBox container) {
        TextFlow  textFlow = new TextFlow(text);
        textFlow.setMaxWidth(Double.MAX_VALUE);
        textFlow.prefWidthProperty().bind(container.widthProperty());
        return textFlow;
    }
}
