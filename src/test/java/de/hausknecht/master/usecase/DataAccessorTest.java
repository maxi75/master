package de.hausknecht.master.usecase;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.entity.service.automata.graph.GraphDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DataAccessorTest {

    private final GraphDataMapper graphDataMapperMock = mock();
    private final DataAccessor classUnderTest = new DataAccessor(graphDataMapperMock);

    @BeforeEach
    void setUp() {
        when(graphDataMapperMock.mapTheoryPageDataExerciseToGraphData(any())).thenReturn(TestDataGenerator.getCorrectGraphData());
    }

    @Nested
    class GetGraphDataFromTheoryPageDataExercise {

        @Test
        void canGetGraphDataFromTheoryPageDataExercise() {
            TheoryPageData.Exercise exercise = TestDataGenerator.getCorrectExerciseData();
            classUnderTest.getGraphDataFromTheoryPageDataExercise(exercise);

            verify(graphDataMapperMock, times(1)).mapTheoryPageDataExerciseToGraphData(exercise);
        }
    }
}