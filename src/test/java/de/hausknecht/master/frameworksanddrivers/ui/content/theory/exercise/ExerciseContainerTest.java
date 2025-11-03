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

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.ExerciseType;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseContainerTest extends UITest {

    private final MultipleChoice multipleChoiceMock = mock();
    private final Word wordMock = mock();
    private final ChangeAutomata changeAutomataMock = mock();
    private final Solution solutionMock = mock();

    private final ExerciseContainer classUnderTest = new ExerciseContainer(multipleChoiceMock, wordMock, changeAutomataMock, solutionMock);

    @Nested
    class AddExercise {

        @Test
        void ExerciseNull() {
            VBox container = new VBox();

            classUnderTest.addExercise(null, container);

            assertTrue(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void getKindIsNull() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(null);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertTrue(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void theoryContainerIsNull() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(null);

            classUnderTest.addExercise(exercise, null);

            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorSolution() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(1)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorMultipleChoice() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(ExerciseType.MULTIPLE_CHOICE);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(1)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorDeaWord() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(ExerciseType.WORD);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(1)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorNeaWord() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(ExerciseType.WORD);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(1)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(0)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorDeaNea() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(ExerciseType.DEA_NEA);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(1)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }

        @Test
        void noErrorNeaDea() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setKind(ExerciseType.NEA_DEA);
            VBox container = new VBox();

            classUnderTest.addExercise(exercise, container);

            assertFalse(container.getChildren().isEmpty());
            verify(multipleChoiceMock, times(0)).addMultipleChoiceExercise(any(), any(), any());
            verify(wordMock, times(0)).addWordExercise(any(), any(), any());
            verify(changeAutomataMock, times(1)).addExercise(any(), any(), any());
            verify(solutionMock, times(0)).addExercise(any(), any(), any());
        }
    }

    @Nested
    class AddTitle {

        @Test
        void addTitle() {
            VBox container = new VBox();

            classUnderTest.addTitle(container, "TITLE");

            assertFalse(container.getChildren().isEmpty());
        }
    }

    @Nested
    class AddQuestion {

        @Test
        void addQuestion() {
            VBox container = new VBox();

            classUnderTest.addQuestion(container, "TITLE");

            assertFalse(container.getChildren().isEmpty());
            assertFalse(((TextFlow) container.getChildren().getFirst()).getChildren().isEmpty());
        }
    }

    @Nested
    class AddTextFlow {

        @Test
        void addTextFlow() {
            VBox container = new VBox();

            TextFlow textFlow = classUnderTest.createTextFlow(new Text("Text"), container);

            assertFalse(textFlow.getChildren().isEmpty());
        }
    }
}
