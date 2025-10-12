package de.hausknecht.master.entity.service.content;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.DfaValues;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.GraphEvaluationResult;
import de.hausknecht.master.entity.service.automata.definition.AutomataGenerator;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import net.automatalib.automaton.fsa.impl.CompactNFA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.hausknecht.master.TestDataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimulatorTest {

    private final AutomataGenerator automataGeneratorMock = mock();
    private final Simulator classUnderTest = new Simulator(automataGeneratorMock);

    @Nested
    class SimulatedNFA {

        @Test
        void nfaInputIsNull() {
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedNFA(null, CHAR_A + " " + CHAR_B + " " + CHAR_C);

            assertFalse(actual.isPresent());
        }

        @Test
        void inputInputIsNull() {
            CompactNFA<String> compactNFAInput = TestDataGenerator.getCorrectNfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedNFA(compactNFAInput, null);

            assertFalse(actual.isPresent());
        }

        @Test
        void inputInputIsBlank() {
            CompactNFA<String> compactNFAInput = TestDataGenerator.getCorrectNfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedNFA(compactNFAInput, " ");

            assertTrue(actual.isPresent());
            assertFalse(actual.get().accepted());
            assertEquals(1, actual.get().nodeID().size());
            assertTrue(actual.get().nodeID().contains(0));
        }

        @Test
        void noInitialStateFound() {
            CompactNFA<String> compactNFAInput = TestDataGenerator.getCorrectNfa(true, true, false, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedNFA(compactNFAInput, " ");

            assertTrue(actual.isPresent());
            assertFalse(actual.get().accepted());
            assertEquals(0, actual.get().nodeID().size());
        }

        @Test
        void withValues() {
            CompactNFA<String> compactNFAInput = TestDataGenerator.getCorrectNfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedNFA(compactNFAInput, CHAR_A + " " + CHAR_B + " " + CHAR_C);

            assertTrue(actual.isPresent());
            assertTrue(actual.get().accepted());
            assertEquals(1, actual.get().nodeID().size());
            assertTrue(actual.get().nodeID().contains(3));
        }
    }

    @Nested
    class SimulatedDFA {

        @Test
        void dfaInputIsNull() {
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedDFA(null, CHAR_A + " " + CHAR_B + " " + CHAR_C);

            assertFalse(actual.isPresent());
        }

        @Test
        void inputInputIsNull() {
            CompactDFA<String> compactDFAInput = TestDataGenerator.getCorrectDfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedDFA(compactDFAInput, null);

            assertFalse(actual.isPresent());
        }

        @Test
        void inputInputIsBlank() {
            CompactDFA<String> compactDFAInput = TestDataGenerator.getCorrectDfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedDFA(compactDFAInput, " ");

            assertTrue(actual.isPresent());
            assertFalse(actual.get().accepted());
            assertEquals(1, actual.get().nodeID().size());
            assertTrue(actual.get().nodeID().contains(0));
        }

        @Test
        void noInitialStateFound() {
            CompactDFA<String> compactDFAInput = TestDataGenerator.getCorrectDfa(true, true, false, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedDFA(compactDFAInput, " ");

            assertFalse(actual.isPresent());
        }

        @Test
        void withValues() {
            CompactDFA<String> compactDFAInput = TestDataGenerator.getCorrectDfa(true, true, true, true);
            Optional<GraphEvaluationResult> actual =  classUnderTest.simulatedDFA(compactDFAInput, CHAR_A + " " + CHAR_B + " " + CHAR_C);

            assertTrue(actual.isPresent());
            assertTrue(actual.get().accepted());
            assertEquals(1, actual.get().nodeID().size());
            assertTrue(actual.get().nodeID().contains(3));
        }
    }

    @Nested
    class FindAcceptingInputForGraphData {

        @BeforeEach
        void setUp() {
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(TestDataGenerator.getCorrectDfaValues(true, true, true, true));
        }

        @Test
        void graphDataInputIsNull() {
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(null);

            assertFalse(actual.isPresent());
        }

        @Test
        void graphDataTransitionsAreNull() {
            GraphData graphDataInput = new GraphData(null, null, null, null);
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void dfaValuesAreNull() {
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(null);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void dfaIsNull() {
            DfaValues dfaValuesTemplate = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            DfaValues dfaValues = new DfaValues(null, dfaValuesTemplate.idToNode(), dfaValuesTemplate.nodeToId());
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(dfaValues);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void initialStateIsNull() {
            DfaValues dfaValuesTemplate = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            CompactDFA<String> dfa = TestDataGenerator.getCorrectDfa(true, true, false, true);
            DfaValues dfaValues = new DfaValues(dfa, dfaValuesTemplate.idToNode(), dfaValuesTemplate.nodeToId());
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(dfaValues);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void firstStateIsAccepting() {
            DfaValues dfaValues = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            dfaValues.dfa().setAccepting(0, true);
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(dfaValues);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertTrue(actual.isPresent());
            assertEquals("", actual.get());
        }

        @Test
        void secondStateIsAccepting() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertTrue(actual.isPresent());
            assertEquals(CHAR_A + CHAR_B, actual.get());
        }
    }
}