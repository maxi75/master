package de.hausknecht.master.frameworksanddrivers.ui.menu;

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

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.frameworksanddrivers.ui.batch.BatchContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.TheoryContainer;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.hausknecht.master.ConstantProvider.POINTS_SPECIAL_EXERCISE;
import static de.hausknecht.master.frameworksanddrivers.ui.menu.MenuOverlay.POINTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuOverlayTest extends UITest {

    private final TheoryContainer theoryContainerMock = mock();
    private final PointSystemAdministrator pointSystemAdministratorMock = mock();
    private final BatchContainer batchContainerMock = mock();

    private final MenuOverlay classUnderTest = new MenuOverlay(theoryContainerMock, pointSystemAdministratorMock, batchContainerMock);

    @BeforeEach
    void setUp() {
        classUnderTest.menuOverlay = new VBox();
        classUnderTest.closeButton = new Button();
        classUnderTest.statusPoints = new Label();
        classUnderTest.batchNavigation = new Button();
        classUnderTest.specialExercise = new Button();
        classUnderTest.specialExercise.setUserData("USERDATA");
        classUnderTest.lockImage = new ImageView();
        classUnderTest.lockImage.setImage(new Image("icon.png"));

        when(pointSystemAdministratorMock.getPoints()).thenReturn(10);
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(Region.USE_PREF_SIZE, classUnderTest.menuOverlay.getMaxWidth());
            assertEquals(Double.MAX_VALUE, classUnderTest.menuOverlay.getMaxHeight());
            assertEquals(50, classUnderTest.menuOverlay.getPadding().getLeft());
            assertEquals(50, classUnderTest.menuOverlay.getPadding().getRight());
            assertEquals(20, classUnderTest.menuOverlay.getPadding().getBottom());
            assertEquals(30, classUnderTest.menuOverlay.getPadding().getTop());
            assertEquals(POINTS + 10, classUnderTest.statusPoints.getText());
            assertNotNull(classUnderTest.lockImage.getImage());
            assertFalse(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void checkUnlockedSpecialExercise() {
            when(pointSystemAdministratorMock.getPoints()).thenReturn(POINTS_SPECIAL_EXERCISE + 10);
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertNull(classUnderTest.lockImage.getImage());
        }

        @Test
        void clickOnCloseButton() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.closeButton.fire();
            });
            fxWait(1000);

            assertFalse(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void clickOnBatchNavigation() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.batchNavigation.fire();
            });
            fxWait(1000);

            verify(batchContainerMock, times(1)).show();
            assertFalse(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void clickOnLockedSpecialExercise() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.specialExercise.fire();
            });
            fxWait(1000);

            verify(theoryContainerMock, times(0)).renderTheoryData(anyString());
            verify(batchContainerMock, times(0)).hide();
            assertTrue(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void clickOnUnlockedSpecialExercise() {
            when(pointSystemAdministratorMock.getPoints()).thenReturn(POINTS_SPECIAL_EXERCISE + 10);
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.specialExercise.fire();
            });
            fxWait(1000);

            verify(theoryContainerMock, times(1)).renderTheoryData(anyString());
            verify(batchContainerMock, times(1)).hide();
            assertFalse(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void clickOnUnlockedSpecialExerciseWithFileIsNull() {
            when(pointSystemAdministratorMock.getPoints()).thenReturn(POINTS_SPECIAL_EXERCISE + 10);
            classUnderTest.specialExercise.setUserData(null);
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.specialExercise.fire();
            });
            fxWait(1000);

            verify(theoryContainerMock, times(0)).renderTheoryData(anyString());
            verify(batchContainerMock, times(0)).hide();
            assertTrue(classUnderTest.menuOverlay.isVisible());
        }

        @Test
        void clickOnUnlockedSpecialExerciseWithFileIsBlank() {
            when(pointSystemAdministratorMock.getPoints()).thenReturn(POINTS_SPECIAL_EXERCISE + 10);
            classUnderTest.specialExercise.setUserData(" ");
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenu();
                classUnderTest.specialExercise.fire();
            });
            fxWait(1000);

            verify(theoryContainerMock, times(0)).renderTheoryData(anyString());
            verify(batchContainerMock, times(0)).hide();
            assertTrue(classUnderTest.menuOverlay.isVisible());
        }
    }

    @Nested
    class OnPointsChanged {

        @Test
        void onPointsChanged() {
            classUnderTest.onPointsChanged(new PointsChangedEvent("message", true));
            fxWait(1000);

            verify(pointSystemAdministratorMock, times(2)).getPoints();
            assertEquals(POINTS + 10, classUnderTest.statusPoints.getText());
        }
    }

    @Nested
    class CloseMenu {

        @Test
        void closeMenu() {
            classUnderTest.menuOverlay.setVisible(true);

            classUnderTest.closeMenu();

            assertFalse(classUnderTest.menuOverlay.isVisible());
        }
    }

    @Nested
    class OpenMenu {

        @Test
        void openMenu() {
            classUnderTest.menuOverlay.setVisible(false);

            classUnderTest.openMenu();

            assertTrue(classUnderTest.menuOverlay.isVisible());
        }
    }
}
