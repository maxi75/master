package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageContainerTest extends UITest {

    private final ClasspathData classpathDataMock = mock();
    private final ErrorHandler errorHandlerMock = mock();

    private final ImageContainer classUnderTest = new ImageContainer(classpathDataMock, errorHandlerMock);

    @BeforeEach
    void setUp() {
        when(classpathDataMock.loadImage(anyString())).thenReturn("icon.png");
    }

    @Nested
    class AddImages {

        @Test
        void getImageNull() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setImage(null);
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(classpathDataMock, times(0)).loadImage(anyString());
        }

        @Test
        void getImageBlank() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setImage(" ");
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(classpathDataMock, times(0)).loadImage(anyString());
        }

        @Test
        void imageStringIsNull() {
            when(classpathDataMock.loadImage(anyString())).thenReturn(null);
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(errorHandlerMock, times(1)).showError(anyString(), anyString(), any());
        }

        @Test
        void imageIsValid() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            assertFalse(container.getChildren().isEmpty());
        }
    }
}