package de.hausknecht.master.entity.service.automata.graph;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.automata.DfaValues;
import de.hausknecht.master.entity.domain.automata.NfaValues;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.hausknecht.master.TestDataGenerator.CORRECT_ANSWERS;
import static org.junit.jupiter.api.Assertions.*;

class ToDotConverterTest {

    private final ToDotConverter classUnderTest = new ToDotConverter();

    @Nested
    class ToDot {

        @Test
        void nfaValueInputNull() throws IOException {
            DfaValues dfaValueInput = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            Set<Integer> highlightStates = new HashSet<>(CORRECT_ANSWERS);
            Boolean accepted = true;

            String actual = classUnderTest.toDot(dfaValueInput, null, highlightStates, accepted);

            assertLinesMatch(
                    List.of(TestDataGenerator.getDot(true, true, false).split("\\R")),
                    List.of(actual.split("\\R")));
        }

        @Test
        void dfaValueInputNull() throws IOException {
            NfaValues nfaValueInput = TestDataGenerator.getCorrectNfaValues(true, true, true, true);
            Set<Integer> highlightStates = new HashSet<>(CORRECT_ANSWERS);
            Boolean accepted = true;

            String actual = classUnderTest.toDot(null, nfaValueInput, highlightStates, accepted);

            assertLinesMatch(
                    List.of(TestDataGenerator.getDot(false, true, false).split("\\R")),
                    List.of(actual.split("\\R")));
        }

        @Test
        void dfaAndNfaValuesNull() throws IOException {
            Set<Integer> highlightStates = new HashSet<>(CORRECT_ANSWERS);
            Boolean accepted = true;

            String actual = classUnderTest.toDot(null, null, highlightStates, accepted);

            assertEquals("", actual);
        }

        @Test
        void highlightStatesNull() throws IOException {
            DfaValues dfaValueInput = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            Boolean accepted = true;

            String actual = classUnderTest.toDot(dfaValueInput, null, null, accepted);

            assertLinesMatch(
                    List.of(TestDataGenerator.getDot(true, false, true).split("\\R")),
                    List.of(actual.split("\\R")));
        }

        @Test
        void highlightStatesIsEmpty() throws IOException {
            DfaValues dfaValueInput = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            Set<Integer> highlightStates = new HashSet<>();
            Boolean accepted = true;

            String actual = classUnderTest.toDot(dfaValueInput, null, highlightStates, accepted);

            assertLinesMatch(
                    List.of(TestDataGenerator.getDot(true, false, false).split("\\R")),
                    List.of(actual.split("\\R")));
        }

        @Test
        void acceptedNull() throws IOException {
            DfaValues dfaValueInput = TestDataGenerator.getCorrectDfaValues(true, true, true, true);
            Set<Integer> highlightStates = new HashSet<>(CORRECT_ANSWERS);

            String actual = classUnderTest.toDot(dfaValueInput, null, highlightStates, null);

            assertLinesMatch(
                    List.of(TestDataGenerator.getDot(true, false, false).split("\\R")),
                    List.of(actual.split("\\R")));
        }
    }
}