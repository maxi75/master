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

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.control.SplitPane;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentContainerControllerTest extends UITest {

    private final ContentContainerController classUnderTest = new ContentContainerController();

    @Nested
    class Initialize {

        @Test
        void initializesGraph() {
            Platform.runLater(() -> {
                assertTrue(SplitPane.isResizableWithParent(classUnderTest.getTheoryContainer()));
                assertTrue(SplitPane.isResizableWithParent(classUnderTest.getSimulatorContainer()));
            });
        }
    }

    @Nested
    class ToggleTheory {

        @Test
        void removeTheoryPane() {
            Platform.runLater(() -> {
                classUnderTest.getContentSplitter().setDividerPositions(1.2);

                classUnderTest.onToggleTheory(null);

                assertEquals(1.2, classUnderTest.getDividerPosition());
                assertFalse(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
                try {
                    assertFalse(classUnderTest.getClass().getField("theoryVisible").getBoolean(classUnderTest));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail();
                }
            });
        }

        @Test
        void addTheoryPane() {
            Platform.runLater(() -> {
                try {
                    classUnderTest.getClass().getField("theoryVisible").setBoolean(classUnderTest, false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail();
                }
                classUnderTest.getContentSplitter().getItems().remove(classUnderTest.getTheoryContainer());
                classUnderTest.getContentSplitter().setDividerPositions(1.2);

                classUnderTest.onToggleTheory(null);

                assertEquals(1.2, classUnderTest.getDividerPosition());
                assertTrue(classUnderTest.getContentSplitter().getItems().contains(classUnderTest.getTheoryContainer()));
                try {
                    assertTrue(classUnderTest.getClass().getField("theoryVisible").getBoolean(classUnderTest));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail();
                }
            });
        }
    }
}
