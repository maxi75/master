package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static de.hausknecht.master.TestDataGenerator.ANSWERS;
import static de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise.Solution.ERROR_WHILE_LOADING_QUESTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class MultipleChoiceTest extends UITest {

    private final PointSystemAdministrator pointSystemAdministratorMock = mock();
    private final ExerciseContainer exerciseContainerMock = mock();
    private final MultipleChoice classUnderTest = new MultipleChoice(pointSystemAdministratorMock);

    @BeforeEach
    void setUp() {
        when(exerciseContainerMock.createTextFlow(any(), any())).thenReturn(new TextFlow());
    }

    @Nested
    class AddSolutionExercise {

        @Test
        void questionIsNull() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setQuestion(null);

            classUnderTest.addMultipleChoiceExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals(ERROR_WHILE_LOADING_QUESTION, captor.getValue());
        }

        @Test
        void questionIsNotNull() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addMultipleChoiceExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals(exercise.getQuestion(), captor.getValue());
        }

        @Test
        void createAnswers() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addMultipleChoiceExercise(exercise, new VBox(), exerciseContainerMock);

            verify(exerciseContainerMock, times(ANSWERS.size())).createTextFlow(any(), any());
        }
    }
}