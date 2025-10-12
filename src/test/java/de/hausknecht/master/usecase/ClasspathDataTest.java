package de.hausknecht.master.usecase;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.service.content.ClasspathLoader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClasspathDataTest {

    private final ClasspathLoader classpathLoaderMock = mock();
    private final ClasspathData classpathData = new ClasspathData(classpathLoaderMock);

    @Nested
    class GetNewPageData {

        @Test
        void getNewPageDataWithValidFile() {
            when(classpathLoaderMock.getJsonFromResource(anyString(), any()))
                    .thenReturn(Optional.of(TestDataGenerator.getCorrectTheoryPageData()));
            assertNotNull(classpathData.getNewPageData("json.json"));
        }

        @Test
        void getNewPageDataWithInvalidFile() {
            when(classpathLoaderMock.getJsonFromResource(anyString(), any()))
                    .thenReturn(Optional.empty());
            assertNull(classpathData.getNewPageData("json.json"));
        }
    }

    @Nested
    class LoadImage {

        @Test
        void getNewPageDataWithValidFile() {
            when(classpathLoaderMock.getStringFromResource(anyString()))
                    .thenReturn(Optional.of(""));
            assertNotNull(classpathData.loadImage("image.svg"));
        }

        @Test
        void getNewPageDataWithInvalidFile() {
            when(classpathLoaderMock.getStringFromResource(anyString()))
                    .thenReturn(Optional.empty());
            assertNull(classpathData.loadImage("image.svg"));
        }
    }
}