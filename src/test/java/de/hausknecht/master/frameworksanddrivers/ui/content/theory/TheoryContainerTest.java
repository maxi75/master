package de.hausknecht.master.frameworksanddrivers.ui.content.theory;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise.ExerciseContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ErrorHandler;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.HeadingContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ImageContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.TextContainer;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TheoryContainerTest extends UITest {

    private final ClasspathData classpathDataMock = mock();
    private final ErrorHandler errorHandlerMock = mock();
    private final ImageContainer imageContainerMock = mock();
    private final HeadingContainer headingContainerMock = mock();
    private final TextContainer textContainerMock = mock();
    private final ExerciseContainer exerciseContainerMock = mock();

    private final TheoryContainer classUnderTest = new TheoryContainer(classpathDataMock, errorHandlerMock, imageContainerMock,
            headingContainerMock, textContainerMock, exerciseContainerMock);

    @BeforeEach
    void setUp() {
        when(classpathDataMock.getNewPageData(anyString())).thenReturn(TestDataGenerator.getCorrectTheoryPageData());
        classUnderTest.theoryContainer = new VBox();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            classUnderTest.theoryContainer.getChildren().add(new HBox());
            classUnderTest.theoryContainer.getChildren().add(new HBox());

            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(100, classUnderTest.theoryContainer.getPadding().getLeft());
            assertEquals(100, classUnderTest.theoryContainer.getPadding().getRight());
            assertEquals(30, classUnderTest.theoryContainer.getPadding().getTop());
            assertEquals(20, classUnderTest.theoryContainer.getPadding().getBottom());
            verify(headingContainerMock, times(1)).addHeadingToTheoryContainer(any(), any());
            verify(headingContainerMock, times(1)).addSubHeading(any(), any());
            verify(textContainerMock, times(1)).addText(any(), any());
            verify(imageContainerMock, times(1)).addImages(any(), any());
            verify(exerciseContainerMock, times(1)).addExercise(any(), any());
        }

        @Test
        void sectionExercisesIsNull() {
            TheoryPageData data = TestDataGenerator.getCorrectTheoryPageData();
            data.getContent().getFirst().setExercises(null);
            when(classpathDataMock.getNewPageData(anyString())).thenReturn(data);
            classUnderTest.theoryContainer.getChildren().add(new HBox());
            classUnderTest.theoryContainer.getChildren().add(new HBox());

            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(100, classUnderTest.theoryContainer.getPadding().getLeft());
            assertEquals(100, classUnderTest.theoryContainer.getPadding().getRight());
            assertEquals(30, classUnderTest.theoryContainer.getPadding().getTop());
            assertEquals(20, classUnderTest.theoryContainer.getPadding().getBottom());
            verify(headingContainerMock, times(1)).addHeadingToTheoryContainer(any(), any());
            verify(headingContainerMock, times(1)).addSubHeading(any(), any());
            verify(textContainerMock, times(1)).addText(any(), any());
            verify(imageContainerMock, times(1)).addImages(any(), any());
            verify(exerciseContainerMock, times(0)).addExercise(any(), any());
        }

        @Test
        void theoryPageDataIsNull() {
            when(classpathDataMock.getNewPageData(anyString())).thenReturn(null);
            classUnderTest.theoryContainer.getChildren().add(new HBox());
            classUnderTest.theoryContainer.getChildren().add(new HBox());

            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(100, classUnderTest.theoryContainer.getPadding().getLeft());
            assertEquals(100, classUnderTest.theoryContainer.getPadding().getRight());
            assertEquals(30, classUnderTest.theoryContainer.getPadding().getTop());
            assertEquals(20, classUnderTest.theoryContainer.getPadding().getBottom());
            verify(headingContainerMock, times(0)).addHeadingToTheoryContainer(any(), any());
            verify(headingContainerMock, times(0)).addSubHeading(any(), any());
            verify(textContainerMock, times(0)).addText(any(), any());
            verify(imageContainerMock, times(0)).addImages(any(), any());
            verify(exerciseContainerMock, times(0)).addExercise(any(), any());
            verify(errorHandlerMock, times(1)).showError(anyString(), anyString(), any());
        }
    }
}