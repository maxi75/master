package de.hausknecht.master.entity.service.automata.graph;

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
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class GraphDataMapperTest {

    private final GraphDataMapper classUnderTest = new GraphDataMapper();

    @Nested
    class MapTheoryPageDataExerciseToGraphData {

        @Test
        void withData() {
            GraphData expected = TestDataGenerator.getCorrectGraphData();
            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void inputNull() {
            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(null);

            assertNull(actual);
        }

        @Test
        void nodesNull() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    null,
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    expectedTemplate.transitions());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setNodes(null);

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void nodesEmpty() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    new ArrayList<>(),
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    expectedTemplate.transitions());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setNodes(new ArrayList<>());

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void startingNodeNull() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    null,
                    expectedTemplate.endingNodes(),
                    expectedTemplate.transitions());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setStartingNode(null);

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void endingNodesNull() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    null,
                    expectedTemplate.transitions());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setEndingNodes(null);

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void endingNodesEmpty() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    new ArrayList<>(),
                    expectedTemplate.transitions());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setEndingNodes(new ArrayList<>());

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void transactionsNull() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    null);

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setTransitions(null);

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void transactionsEmpty() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    new ArrayList<>());

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.setTransitions(new ArrayList<>());

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void transactionsNotNullButSingleTransactionNull() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            List<TransitionTriple> transitionTriples = expectedTemplate.transitions();
            transitionTriples.remove(1);

            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    transitionTriples);

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.getTransitions().remove(1);

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void transactionMalformed() {
            GraphData expectedTemplate = TestDataGenerator.getCorrectGraphData();
            List<TransitionTriple> transitionTriples = expectedTemplate.transitions();
            transitionTriples.remove(1);

            GraphData expected = new GraphData(
                    expectedTemplate.availableNodes(),
                    expectedTemplate.startingNode(),
                    expectedTemplate.endingNodes(),
                    transitionTriples);

            TheoryPageData.Exercise input = TestDataGenerator.getCorrectExerciseData();
            input.getTransitions().set(1, "Malformed");

            GraphData actual = classUnderTest.mapTheoryPageDataExerciseToGraphData(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
