package de.hausknecht.master.frameworksanddrivers.ui.content.theory;

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
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentType;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise.ExerciseContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ErrorHandler;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.HeadingContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ImageContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.TextContainer;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

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
    private final ApplicationEventPublisher applicationEventPublisherMock = mock();

    private final TheoryContainer classUnderTest = new TheoryContainer(classpathDataMock, errorHandlerMock, imageContainerMock,
            headingContainerMock, textContainerMock, exerciseContainerMock, applicationEventPublisherMock);

    @BeforeEach
    void setUp() {
        when(classpathDataMock.getNewPageData(anyString())).thenReturn(TestDataGenerator.getCorrectTheoryPageData());
        classUnderTest.theoryContainer = new VBox();
        classUnderTest.fullsize = new Button();
        classUnderTest.fullsize.setText("⤡");
        classUnderTest.backButton = new Button();
        classUnderTest.backButton.setVisible(true);
        classUnderTest.backButton.setManaged(true);
        classUnderTest.nextButton = new Button();
        classUnderTest.nextButton.setVisible(true);
        classUnderTest.nextButton.setManaged(true);
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

    @Nested
    class OnFullsizeClicked {

        @Test
        void checkEventPublication() {
            classUnderTest.toggleTheory();

            ArgumentCaptor<ToggleContentEvent> captor = ArgumentCaptor.forClass(ToggleContentEvent.class);
            verify(applicationEventPublisherMock, times(1)).publishEvent(captor.capture());
            assertNotNull(captor.getValue());
            assertEquals(ToggleContentType.THEORY, captor.getValue().type());
        }

        @Test
        void checkIconToggle() {
            assertEquals("⤡", classUnderTest.fullsize.getText());

            classUnderTest.toggleTheory();
            assertEquals("⤢", classUnderTest.fullsize.getText());

            classUnderTest.toggleTheory();
            fxWait(200);
            assertEquals("⤡", classUnderTest.fullsize.getText());
        }
    }

    @Nested
    class BackButtonClicked {

        @Test
        void onFirstPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.getFirst();
                classUnderTest.back();
            });

            fxWait(200);
            verify(classpathDataMock, times(0)).getNewPageData(classUnderTest.currentFile);
        }

        @Test
        void onSecondPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.get(1);
                classUnderTest.back();
            });

            fxWait(200);
            verify(classpathDataMock, times(1)).getNewPageData(classUnderTest.currentFile);
            assertFalse(classUnderTest.backButton.isVisible());
            assertFalse(classUnderTest.backButton.isManaged());
            assertTrue(classUnderTest.nextButton.isVisible());
            assertTrue(classUnderTest.nextButton.isManaged());
        }

        @Test
        void onLastPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.getLast();
                classUnderTest.back();
            });

            fxWait(200);
            verify(classpathDataMock, times(1)).getNewPageData(classUnderTest.currentFile);
            assertTrue(classUnderTest.backButton.isVisible());
            assertTrue(classUnderTest.backButton.isManaged());
            assertTrue(classUnderTest.nextButton.isVisible());
            assertTrue(classUnderTest.nextButton.isManaged());
        }
    }

    @Nested
    class NextButtonClicked {

        @Test
        void onFirstPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.getFirst();
                classUnderTest.next();
            });

            fxWait(200);
            verify(classpathDataMock, times(1)).getNewPageData(classUnderTest.currentFile);
            assertTrue(classUnderTest.backButton.isVisible());
            assertTrue(classUnderTest.backButton.isManaged());
            assertTrue(classUnderTest.nextButton.isVisible());
            assertTrue(classUnderTest.nextButton.isManaged());
        }

        @Test
        void onSecondLastPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.get(TheoryPages.PAGES.size() - 2);
                classUnderTest.next();
            });

            fxWait(200);
            verify(classpathDataMock, times(1)).getNewPageData(classUnderTest.currentFile);
            assertTrue(classUnderTest.backButton.isVisible());
            assertTrue(classUnderTest.backButton.isManaged());
            assertFalse(classUnderTest.nextButton.isVisible());
            assertFalse(classUnderTest.nextButton.isManaged());
        }

        @Test
        void onLastPage() {
            Platform.runLater(() -> {
                classUnderTest.currentFile = TheoryPages.PAGES.getLast();
                classUnderTest.next();
            });

            fxWait(200);
            verify(classpathDataMock, times(0)).getNewPageData(classUnderTest.currentFile);
        }
    }


    @Nested
    class RenderTheoryData {

        @Test
        void onUnknownPage() {
            Platform.runLater(() -> {
                classUnderTest.renderTheoryData("UnknownPage");
            });

            fxWait(200);
            verify(classpathDataMock, times(1)).getNewPageData(classUnderTest.currentFile);
            assertFalse(classUnderTest.backButton.isVisible());
            assertFalse(classUnderTest.backButton.isManaged());
            assertFalse(classUnderTest.nextButton.isVisible());
            assertFalse(classUnderTest.nextButton.isManaged());
        }
    }
}
