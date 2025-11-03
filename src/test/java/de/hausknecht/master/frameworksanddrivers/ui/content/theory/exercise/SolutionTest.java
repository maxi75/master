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
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.NodeDefinitionContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.TransitionContainer;
import de.hausknecht.master.usecase.DataAccessor;
import de.hausknecht.master.usecase.GraphAdministrator;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise.Solution.ERROR_WHILE_LOADING_QUESTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class SolutionTest extends UITest {

    private final DataAccessor dataAccessorMock = mock();
    private final NodeContainer nodeContainerMock = mock();
    private final NodeDefinitionContainer nodeDefinitionContainerMock = mock();
    private final TransitionContainer transitionContainerMock = mock();
    private final GraphAdministrator graphAdministratorMock = mock();
    private final PointSystemAdministrator pointSystemAdministratorMock = mock();
    private final ExerciseContainer exerciseContainerMock = mock();

    private final Solution classUnderTest = new Solution(dataAccessorMock, nodeContainerMock, nodeDefinitionContainerMock,
            transitionContainerMock, graphAdministratorMock, pointSystemAdministratorMock);

    @BeforeEach
    void setUp() {
        when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(TestDataGenerator.getCorrectGraphData());
    }

    @Nested
    class AddMultipleChoiceExercise {

        @Test
        void graphDataIsNull() {
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(null);
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addExercise(exercise, new VBox(), exerciseContainerMock);

            verify(exerciseContainerMock, times(0)).addTitle(any(), anyString());
        }

        @Test
        void questionIsNull() {
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(TestDataGenerator.getCorrectGraphData());
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            exercise.setQuestion(null);

            classUnderTest.addExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals(ERROR_WHILE_LOADING_QUESTION, captor.getValue());
        }

        @Test
        void questionIsNotNull() {
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(TestDataGenerator.getCorrectGraphData());
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals(exercise.getQuestion(), captor.getValue());
        }
    }
}
