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
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListElementTest extends UITest {
    private static final String LABEL_TEXT = "TEXT";
    private static final String NEW_TEXT = "NEW TEXT";

    private final ListElementContainer listElementContainerMock = mock();
    private final ListElement classUnderTest = new ListElement();

    @BeforeEach
    void setUp() {
        classUnderTest.setListElementContainer(listElementContainerMock);
        classUnderTest.listElementName = new Label(LABEL_TEXT);
        classUnderTest.deleteBtn = new Button();
    }

    @Nested
    class Initialize {

        @Test
        void initialize() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.deleteBtn.fire();
            });
            fxWait(1000);

            verify(listElementContainerMock, times(1)).delete(LABEL_TEXT, null);
        }
    }

    @Nested
    class SetListName {

        @Test
        void setListName() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            classUnderTest.setListName(NEW_TEXT);

            assertEquals(NEW_TEXT, classUnderTest.listElementName.getText());
        }
    }
}
