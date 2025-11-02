package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.DataAccessor;
import de.hausknecht.master.usecase.GraphAdministrator;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class WordTest extends UITest {

    private final DataAccessor dataAccessorMock = mock();
    private final GraphAdministrator graphAdministratorMock = mock();
    private final PointSystemAdministrator pointSystemAdministratorMock = mock();
    private final ExerciseContainer exerciseContainerMock = mock();

    private final Word classUnderTest = new Word(dataAccessorMock, graphAdministratorMock, pointSystemAdministratorMock);

    @BeforeEach
    void setUp() {
        when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(TestDataGenerator.getCorrectGraphData());
    }

    @Nested
    class AddWordExercise {

        @Test
        void graphDataIsNull() {
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(null);
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addWordExercise(exercise, new VBox(), exerciseContainerMock);

            verify(exerciseContainerMock, times(0)).addTitle(any(), anyString());
        }

        @Test
        void buildQuestionWithTransitionsIsNull() {
            GraphData dataTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData data = new GraphData(dataTemplate.availableNodes(),
                    dataTemplate.startingNode(),
                    dataTemplate.endingNodes(),
                    null);
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(data);

            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addWordExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals("""
                    Gegeben ist folgende Automatendefinition:\s
                    A=({Error: Fehler beim Ermitteln der Transitions}, {0, 1, 2, 3}, δ, 0, {2, 3})\s
                    
                    Welche beispielhafte Eingabefolge wird von dem Automaten akzeptiert?\s
                    """, captor.getValue());
        }

        @Test
        void buildQuestionWithTransitionIsNull() {
            GraphData data = TestDataGenerator.getCorrectGraphData();
            data.transitions().set(2, null);
            when(dataAccessorMock.getGraphDataFromTheoryPageDataExercise(any())).thenReturn(data);

            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();

            classUnderTest.addWordExercise(exercise, new VBox(), exerciseContainerMock);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(exerciseContainerMock).addQuestion(any(), captor.capture());
            assertEquals("""
                    Gegeben ist folgende Automatendefinition:\s
                    A=({a, b, c}, {0, 1, 2, 3}, δ, 0, {2, 3})\s
                    
                    mit der Zustandsübergangsfunktion:\s
                    δ(0,a) = 1;   δ(1,b) = δ(3,c) = 2;   δ(3,c) = 3;   \s
                    
                    Welche beispielhafte Eingabefolge wird von dem Automaten akzeptiert?\s
                    """, captor.getValue());
        }
    }
}