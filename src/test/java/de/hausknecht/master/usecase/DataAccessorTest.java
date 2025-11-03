package de.hausknecht.master.usecase;

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
