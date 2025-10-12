package de.hausknecht.master.entity.service.automata.graph;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.automata.GraphEvaluationResult;
import de.hausknecht.master.entity.service.automata.definition.AutomataGenerator;
import de.hausknecht.master.entity.service.content.Simulator;
import net.automatalib.automaton.fsa.impl.CompactDFA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GraphRenderingTest {

    private final ToDotConverter toDotConverterMock = mock();
    private final AutomataGenerator automataGeneratorMock = mock();
    private final Simulator simulatorMock = mock();
    private final GraphRendering classUnderTest = new GraphRendering(toDotConverterMock, automataGeneratorMock, simulatorMock);

    @BeforeEach
    void setUp() throws IOException {
        when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(TestDataGenerator.getCorrectDfaValues(true, true, true, true));
        when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(TestDataGenerator.getCorrectNfaValues(true, true, true, true));
        when(automataGeneratorMock.generateCompactDFA(isNull())).thenReturn(null);
        when(automataGeneratorMock.generateCompactNFA(isNull())).thenReturn(null);
        when(simulatorMock.simulatedDFA(any(), anyString())).thenReturn(TestDataGenerator.getGraphEvaluationResultOptional());
        when(simulatorMock.simulatedNFA(any(), anyString())).thenReturn(TestDataGenerator.getGraphEvaluationResultOptional());
        when(simulatorMock.simulatedDFA(any(), isNull())).thenReturn(Optional.empty());
        when(simulatorMock.simulatedNFA(any(), isNull())).thenReturn(Optional.empty());
        when(toDotConverterMock.toDot(any(), any(), any(), anyBoolean())).thenReturn("Return value");
        when(toDotConverterMock.toDot(any(), isNull(), isNull(), isNull())).thenReturn(null);
    }

    @Nested
    class DfaToDot {

        @Test
        void withValues() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            Optional<String> actual = classUnderTest.dfaToDot(graphDataInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), isNull());
            verify(toDotConverterMock, times(1)).toDot(any(), isNull(), isNull(), isNull());
            assertThat(actual).isEqualTo(Optional.empty());
        }

        @Test
        void graphDataNull() throws IOException {
            Optional<String> actual = classUnderTest.dfaToDot(null);

            verify(automataGeneratorMock, times(0)).generateCompactDFA(any());
            verify(simulatorMock, times(0)).simulatedDFA(any(), isNull());
            verify(toDotConverterMock, times(0)).toDot(any(), isNull(), isNull(), isNull());
            assertThat(actual).isEqualTo(Optional.empty());
        }
    }

    @Nested
    class SimulatedDFAToDot {

        @Test
        void withValues() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedDFAToDot(graphDataInput, stringInput));

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(any(), any(), any(), anyBoolean());
        }

        @Test
        void graphDataNull() {
            String stringInput = "SampleString";

            assertThat(classUnderTest.simulatedDFAToDot(null, stringInput)).isEmpty();
        }

        @Test
        void inputNull() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedDFAToDot(graphDataInput, null));

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), isNull());
            verify(toDotConverterMock, times(1)).toDot(any(), isNull(), isNull(), isNull());
        }

        @Test
        void simulationResultNodeIDNull() throws IOException {
            Optional<GraphEvaluationResult> optional = Optional.of(new GraphEvaluationResult(null, true));
            when(simulatorMock.simulatedDFA(any(), anyString())).thenReturn(optional);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedDFAToDot(graphDataInput, stringInput));

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(any(), isNull(), isNull(), anyBoolean());
        }

        @Test
        void errorInToDotConverter() throws IOException {
            when(toDotConverterMock.toDot(any(), any(), any(), anyBoolean())).thenThrow(new IOException());
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            Optional<String> actual =  classUnderTest.simulatedDFAToDot(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(any(), any(), any(), anyBoolean());
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class GetCompactDFAFromGraphData {

        @Test
        void withValues() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            CompactDFA<String> actual =  classUnderTest.getCompactDFAFromGraphData(graphDataInput);

            assertThat(actual).isNotNull();
        }

        @Test
        void graphDataNull() {
            CompactDFA<String> actual =  classUnderTest.getCompactDFAFromGraphData(null);

            assertThat(actual).isNull();
        }

        @Test
        void automataGeneratorResultIsNull() {
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(null);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            CompactDFA<String> actual =  classUnderTest.getCompactDFAFromGraphData(graphDataInput);

            assertThat(actual).isNull();
        }
    }

    @Nested
    class AcceptsInputDFA {

        @Test
        void withValues() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputDFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), anyString());
            assertTrue(result);
        }

        @Test
        void graphDataNull() {
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputDFA(null, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(0)).simulatedDFA(any(), anyString());
            assertFalse(result);
        }

        @Test
        void inputNull() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            boolean result = classUnderTest.acceptsInputDFA(graphDataInput, null);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), isNull());
            assertFalse(result);
        }

        @Test
        void dfaValuesNull() {
            when(automataGeneratorMock.generateCompactDFA(any())).thenReturn(null);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputDFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(0)).simulatedDFA(any(), anyString());
            assertFalse(result);
        }

        @Test
        void simulationResultEmpty() {
            when(simulatorMock.simulatedDFA(any(), anyString())).thenReturn(Optional.empty());
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputDFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactDFA(any());
            verify(simulatorMock, times(1)).simulatedDFA(any(), anyString());
            assertFalse(result);
        }
    }

    @Nested
    class NfaToDot {

        @Test
        void withValues() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            Optional<String> actual = classUnderTest.nfaToDot(graphDataInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), isNull());
            verify(toDotConverterMock, times(1)).toDot(isNull(), any(), isNull(), isNull());
            assertThat(actual).isEqualTo(Optional.empty());
        }

        @Test
        void graphDataNull() throws IOException {
            Optional<String> actual = classUnderTest.nfaToDot(null);

            verify(automataGeneratorMock, times(0)).generateCompactNFA(any());
            verify(simulatorMock, times(0)).simulatedNFA(any(), isNull());
            verify(toDotConverterMock, times(0)).toDot(isNull(), any(), isNull(), isNull());
            assertThat(actual).isEqualTo(Optional.empty());
        }
    }

    @Nested
    class SimulatedNFAToDot {

        @Test
        void withValues() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedNFAToDot(graphDataInput, stringInput));

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(any(), any(), any(), anyBoolean());
        }

        @Test
        void graphDataNull() {
            String stringInput = "SampleString";

            assertThat(classUnderTest.simulatedNFAToDot(null, stringInput)).isEmpty();
        }

        @Test
        void inputNull() throws IOException {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedNFAToDot(graphDataInput, null));

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), isNull());
            verify(toDotConverterMock, times(1)).toDot(isNull(), any(), isNull(), isNull());
        }

        @Test
        void simulationResultNodeIDNull() throws IOException {
            Optional<GraphEvaluationResult> optional = Optional.of(new GraphEvaluationResult(null, true));
            when(simulatorMock.simulatedNFA(any(), anyString())).thenReturn(optional);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            assertThatNoException().isThrownBy(() -> classUnderTest.simulatedNFAToDot(graphDataInput, stringInput));

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(isNull(), any(), isNull(), anyBoolean());
        }

        @Test
        void errorInToDotConverter() throws IOException {
            when(toDotConverterMock.toDot(any(), any(), any(), anyBoolean())).thenThrow(new IOException());
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            Optional<String> actual =  classUnderTest.simulatedNFAToDot(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), anyString());
            verify(toDotConverterMock, times(1)).toDot(any(), any(), any(), anyBoolean());
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class AcceptsInputNFA {

        @Test
        void withValues() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputNFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), anyString());
            assertTrue(result);
        }

        @Test
        void graphDataNull() {
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputNFA(null, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(0)).simulatedNFA(any(), anyString());
            assertFalse(result);
        }

        @Test
        void inputNull() {
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();

            boolean result = classUnderTest.acceptsInputNFA(graphDataInput, null);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), isNull());
            assertFalse(result);
        }

        @Test
        void nfaValuesNull() {
            when(automataGeneratorMock.generateCompactNFA(any())).thenReturn(null);
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputNFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(0)).simulatedNFA(any(), anyString());
            assertFalse(result);
        }

        @Test
        void simulationResultEmpty() {
            when(simulatorMock.simulatedNFA(any(), anyString())).thenReturn(Optional.empty());
            GraphData graphDataInput = TestDataGenerator.getCorrectGraphData();
            String stringInput = "SampleString";

            boolean result = classUnderTest.acceptsInputNFA(graphDataInput, stringInput);

            verify(automataGeneratorMock, times(1)).generateCompactNFA(any());
            verify(simulatorMock, times(1)).simulatedNFA(any(), anyString());
            assertFalse(result);
        }
    }
}