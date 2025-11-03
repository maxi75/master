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
import de.hausknecht.master.usecase.NodeAdministrator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NodeContainerTest extends UITest {
    private static final String TEXT = "TEXT";

    private final NodeAdministrator nodeAdministratorMock = mock();
    private final NodeContainer classUnderTest = new NodeContainer(nodeAdministratorMock);

    @BeforeEach
    void setUp(){
        when(nodeAdministratorMock.addNode(anyString())).thenReturn(true);

        classUnderTest.listElementContainer = new VBox();
        classUnderTest.addButton = new Button();
        classUnderTest.nameField = new TextField();
        classUnderTest.nameField.setText(TEXT);
    }

    @Nested
    class Initialize {

        @Test
        void addButtonWithAlreadyExistingNode() {
            when(nodeAdministratorMock.addNode(anyString())).thenReturn(false);

            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.addButton.fire();
            });

            fxWait(1000);
            assertEquals(0, classUnderTest.items.size());
        }
    }

    @Nested
    class DeleteAll {

        @Test
        void deleteAll() {
            classUnderTest.items.put(TEXT, classUnderTest.listElementContainer);

            classUnderTest.deleteAll();

            verify(nodeAdministratorMock, times(1)).removeNodeFromNodeRegistry(TEXT);
        }
    }
}
