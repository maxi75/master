package de.hausknecht.master.entity.service.content;

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
import de.hausknecht.master.entity.domain.automata.GraphEvaluationResult;
import de.hausknecht.master.entity.domain.automata.NfaValues;
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
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(TestDataGenerator.getCorrectNfaValues(true, true, true, true));
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
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(null);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void dfaIsNull() {
            NfaValues nfaValuesTemplate = TestDataGenerator.getCorrectNfaValues(true, true, true, true);
            NfaValues dfaValues = new NfaValues(null, nfaValuesTemplate.idToNode(), nfaValuesTemplate.nodeToId());
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(dfaValues);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void initialStateIsNull() {
            NfaValues nfaValuesTemplate = TestDataGenerator.getCorrectNfaValues(true, true, true, true);
            CompactNFA<String> nfa = TestDataGenerator.getCorrectNfa(true, true, false, true);
            NfaValues nfaValues = new NfaValues(nfa, nfaValuesTemplate.idToNode(), nfaValuesTemplate.nodeToId());
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(nfaValues);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            Optional<String> actual = classUnderTest.findAcceptingInputForGraphData(graphDataInput);

            assertFalse(actual.isPresent());
        }

        @Test
        void firstStateIsAccepting() {
            NfaValues nfaValues = TestDataGenerator.getCorrectNfaValues(true, true, true, true);
            nfaValues.nfa().setAccepting(0, true);
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(nfaValues);
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
