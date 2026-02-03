package de.hausknecht.master.frameworksanddrivers.ui.content;

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

import de.hausknecht.master.entity.domain.eventdata.ToggleContentEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentType;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentContainerControllerTest extends UITest {

    private ContentContainerController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ContentContainerController();
        classUnderTest.contentSplitter = new SplitPane();
        classUnderTest.theoryContainer = new VBox();
        classUnderTest.simulatorContainer = new VBox();
        classUnderTest.contentSplitter.getItems().add(classUnderTest.theoryContainer);
        classUnderTest.contentSplitter.getItems().add(classUnderTest.simulatorContainer);
    }

    @Nested
    class Initialize {

        @Test
        void initializesGraph() {
            Platform.runLater(() -> {
                assertTrue(SplitPane.isResizableWithParent(classUnderTest.getTheoryContainer()));
                assertTrue(SplitPane.isResizableWithParent(classUnderTest.getSimulatorContainer()));
                assertFalse(classUnderTest.isSimulationVisible());
            });
        }
    }

    @Nested
    class ToggleTheory {

        @Test
        void removeTheoryPane() {
            Platform.runLater(() -> {
                classUnderTest.contentSplitter.setDividerPositions(1.2);
                classUnderTest.handleToggle(new ToggleContentEvent(ToggleContentType.SIMULATION));
            });

            fxWait(200);
            assertEquals(1.2, classUnderTest.getDividerPosition());
            assertFalse(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
            assertFalse(classUnderTest.theoryVisible);
        }

        @Test
        void addTheoryPane() {
            Platform.runLater(() -> {
                classUnderTest.theoryVisible = false;
                classUnderTest.getContentSplitter().getItems().remove(classUnderTest.getTheoryContainer());

                classUnderTest.handleToggle(new ToggleContentEvent(ToggleContentType.SIMULATION));
            });

            fxWait(200);
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
            assertTrue(classUnderTest.simulationVisible);
        }

        @Test
        void removeSimulationPane() {
            Platform.runLater(() -> {
                classUnderTest.contentSplitter.setDividerPositions(1.2);
                classUnderTest.handleToggle(new ToggleContentEvent(ToggleContentType.THEORY));
            });

            fxWait(200);
            assertEquals(1.2, classUnderTest.getDividerPosition());
            assertFalse(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getSimulatorContainer()));
            assertFalse(classUnderTest.simulationVisible);
        }

        @Test
        void addSimulationPane() {
            Platform.runLater(() -> {
                classUnderTest.simulationVisible = false;
                classUnderTest.getContentSplitter().getItems().remove(classUnderTest.getSimulatorContainer());

                classUnderTest.handleToggle(new ToggleContentEvent(ToggleContentType.THEORY));
            });

            fxWait(200);
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getSimulatorContainer()));
            assertTrue(classUnderTest.simulationVisible);
        }

        @Test
        void eventIsNull() {
            fxWait(200);
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getSimulatorContainer()));

            Platform.runLater(() -> classUnderTest.handleToggle(null));

            fxWait(200);
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
            assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getSimulatorContainer()));
        }
    }
}
