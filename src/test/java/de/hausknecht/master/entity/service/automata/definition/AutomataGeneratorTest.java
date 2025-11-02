package de.hausknecht.master.entity.service.automata.definition;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AutomataGeneratorTest {

    private final AutomataGenerator classUnderTest = new AutomataGenerator();

    @Nested
    class NFATest {

        @Test
        void withValues() {
            NfaValues expected = TestDataGenerator.getCorrectNfaValues(true, true, true, true);
            GraphData input = TestDataGenerator.getCorrectGraphData();

            NfaValues actual = classUnderTest.generateCompactNFA(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void graphDataIsNull() {
            NfaValues actual = classUnderTest.generateCompactNFA(null);
            assertThat(actual).isNull();
        }

        @Test
        void transitionsAreEmpty() {
            NfaValues expected = TestDataGenerator.getCorrectNfaValues(false, true, true, true);
            GraphData input = TestDataGenerator.getCorrectGraphData();
            input.transitions().removeAll(input.transitions());

            NfaValues actual = classUnderTest.generateCompactNFA(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void availableNodesAreEmpty() {
            NfaValues expected = TestDataGenerator.getCorrectNfaValues(true, false, true, true);
            expected.nodeToId().clear();
            expected.idToNode().clear();

            GraphData input = TestDataGenerator.getCorrectGraphData();
            input.availableNodes().removeAll(input.availableNodes());

            NfaValues actual = classUnderTest.generateCompactNFA(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void startingNodeIsNull() {
            NfaValues expected = TestDataGenerator.getCorrectNfaValues(true, true, false, true);
            GraphData inputTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData input = new GraphData(inputTemplate.availableNodes(),
                    null,
                    inputTemplate.endingNodes(),
                    inputTemplate.transitions());

            NfaValues actual = classUnderTest.generateCompactNFA(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void transitionsIsNull() {
            GraphData inputTemplate = TestDataGenerator.getCorrectGraphData();
            GraphData input = new GraphData(inputTemplate.availableNodes(),
                    inputTemplate.startingNode(),
                    inputTemplate.endingNodes(),
                    null);

            NfaValues actual = classUnderTest.generateCompactNFA(input);
            assertThat(actual).isNull();
        }

        @Test
        void transitionsIsEmpty() {
            NfaValues expected = TestDataGenerator.getCorrectNfaValues(true, true, true, false);
            GraphData input = TestDataGenerator.getCorrectGraphData();
            input.transitions().removeAll(input.transitions());

            NfaValues actual = classUnderTest.generateCompactNFA(input);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}