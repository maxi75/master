package de.hausknecht.master.entity.service.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClasspathLoaderTest {

    private final ObjectMapper objectMapperMock = mock();
    private final ClasspathLoader classUnderTest = new ClasspathLoader(objectMapperMock);

    @Nested
    class GetJsonFromResource {

        @BeforeEach
        void setUp() throws IOException {
            when(objectMapperMock.readValue(any(URL.class), any(Class.class))).thenReturn(TestDataGenerator.getCorrectTheoryPageData());
        }

        @Test
        void withValue() {
            TheoryPageData expected = TestDataGenerator.getCorrectTheoryPageData();
            Optional<TheoryPageData> actual = classUnderTest.getJsonFromResource("theory/json/chapterOne/alphabet.json", TheoryPageData.class);

            assertTrue(actual.isPresent());
            assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void withNotExistingPath() {
            Optional<TheoryPageData> actual = classUnderTest.getJsonFromResource("notExistingPath", TheoryPageData.class);

            assertFalse(actual.isPresent());
        }

        @Test
        void fileNameNull() {
            Optional<TheoryPageData> actual = classUnderTest.getJsonFromResource(null, TheoryPageData.class);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class GetStringFromResource {

        @Test
        void withValue() {
            assertThatNoException().isThrownBy(() -> classUnderTest.getStringFromResource("path"));
        }

        @Test
        void fileNameNull() {
            Optional<String> actual = classUnderTest.getStringFromResource(null);

            assertThat(actual).isEmpty();
        }
    }
}