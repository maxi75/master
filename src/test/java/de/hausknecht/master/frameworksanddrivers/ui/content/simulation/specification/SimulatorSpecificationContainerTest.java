package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification.SimulatorSpecificationContainer.*;
import static org.junit.jupiter.api.Assertions.*;

class SimulatorSpecificationContainerTest extends UITest {

    private final SimulatorSpecificationContainer classUnderTest = new SimulatorSpecificationContainer();

    @BeforeEach
    void setUp() {
        classUnderTest.specificationContainer = new VBox();
        classUnderTest.specificationContent = new HBox();
        classUnderTest.specificationTabBtn = new Button();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(COLLAPSED, classUnderTest.specificationContainer.getPrefHeight());
            assertTrue(classUnderTest.specificationContainer.isVisible());
            assertTrue(classUnderTest.specificationContainer.isManaged());
            assertEquals(ARROW_UP, classUnderTest.specificationTabBtn.getText());
            assertFalse(classUnderTest.expanded);
        }

        @Test
        void expand() {

            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.specificationTabBtn.fire();
            });
            fxWait(1000);

            assertEquals(EXPANDED, classUnderTest.specificationContainer.getPrefHeight());
            assertTrue(classUnderTest.specificationContainer.isVisible());
            assertTrue(classUnderTest.specificationContainer.isManaged());
            assertEquals(ARROW_DOWN, classUnderTest.specificationTabBtn.getText());
            assertTrue(classUnderTest.expanded);
        }
    }
}
