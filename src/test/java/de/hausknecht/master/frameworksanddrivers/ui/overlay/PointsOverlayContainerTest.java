package de.hausknecht.master.frameworksanddrivers.ui.overlay;

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

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.hausknecht.master.frameworksanddrivers.ui.overlay.PointsOverlayContainer.CSS_CLASS_FAILURE;
import static de.hausknecht.master.frameworksanddrivers.ui.overlay.PointsOverlayContainer.CSS_CLASS_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

class PointsOverlayContainerTest extends UITest {

    private final PointsOverlayContainer classUnderTest = new PointsOverlayContainer();

    @BeforeEach
    void setUp() {
        classUnderTest.overlayContainer = new StackPane();
        classUnderTest.overlayMessage = new Label();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertFalse(classUnderTest.overlayMessage.isVisible());
        }
    }

    @Nested
    class Show {

        @Test
        void showWithSuccessfulTrue() {
            classUnderTest.overlayMessage.getStyleClass().add(CSS_CLASS_SUCCESS);
            classUnderTest.overlayMessage.getStyleClass().add(CSS_CLASS_FAILURE);

            classUnderTest.show("message", true);

            assertTrue(classUnderTest.overlayMessage.isVisible());
            assertEquals("message",  classUnderTest.overlayMessage.getText());
            assertTrue(classUnderTest.overlayMessage.getStyleClass().contains(CSS_CLASS_SUCCESS));
            assertFalse(classUnderTest.overlayMessage.getStyleClass().contains(CSS_CLASS_FAILURE));
        }

        @Test
        void showWithSuccessfulFalse() {
            classUnderTest.overlayMessage.getStyleClass().add(CSS_CLASS_SUCCESS);
            classUnderTest.overlayMessage.getStyleClass().add(CSS_CLASS_FAILURE);

            classUnderTest.show("message", false);

            assertTrue(classUnderTest.overlayMessage.isVisible());
            assertEquals("message",  classUnderTest.overlayMessage.getText());
            assertFalse(classUnderTest.overlayMessage.getStyleClass().contains(CSS_CLASS_SUCCESS));
            assertTrue(classUnderTest.overlayMessage.getStyleClass().contains(CSS_CLASS_FAILURE));
        }
    }

    @Nested
    class Hide {

        @Test
        void hide() {
            classUnderTest.overlayMessage.setVisible(true);

            classUnderTest.hide();

            assertFalse(classUnderTest.overlayMessage.isVisible());
        }
    }
}
