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

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemovedEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.NodeAdministrator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NodeDefinitionContainerTest extends UITest {
    private static final List<String> NODES = List.of("ABC", "DEF");
    private static final ObservableList<String> OBSERVABLE_NODES = FXCollections.observableList(NODES);

    private final NodeAdministrator nodeAdministratorMock = mock();
    private final NodeDefinitionContainer classUnderTest = new NodeDefinitionContainer(nodeAdministratorMock);

    @BeforeEach
    void setUp() {
        when(nodeAdministratorMock.getNodes()).thenReturn(OBSERVABLE_NODES);
        when(nodeAdministratorMock.addEndingNode(anyString())).thenReturn(true);

        classUnderTest.listElementContainer = new VBox();
        classUnderTest.addButton = new Button();
        classUnderTest.startingNode = new ComboBox<>();
        classUnderTest.endingNode = new ComboBox<>();
    }

    @Nested
    class Initialize {

        @Test
        void checkDefaultValues() {
            Platform.runLater(classUnderTest::initialize);

            fxWait(1000);
            assertThat(classUnderTest.startingNode.getItems()).containsAll(NODES);
            assertThat(classUnderTest.endingNode.getItems()).containsAll(NODES);
        }

        @Test
        void addStartingNode() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.startingNode.setValue(OBSERVABLE_NODES.getFirst());
                Event.fireEvent(classUnderTest.startingNode, new ActionEvent());
            });

            fxWait(1000);
            verify(nodeAdministratorMock, times(1)).setStartingNode(OBSERVABLE_NODES.getFirst());
        }

        @Test
        void addEndingNode() {
            assertThatNoException().isThrownBy(() -> {
                Platform.runLater(() -> {
                    classUnderTest.initialize();
                    classUnderTest.endingNode.setValue(OBSERVABLE_NODES.getFirst());
                    Event.fireEvent(classUnderTest.endingNode, new ActionEvent());
                });

                fxWait(1000);
            });
        }

        @Test
        void endingNodeAlreadyExists() {
            when(nodeAdministratorMock.addEndingNode(anyString())).thenReturn(false);

            assertThatNoException().isThrownBy(() -> {
                Platform.runLater(() -> {
                    classUnderTest.initialize();
                    classUnderTest.endingNode.setValue(OBSERVABLE_NODES.getFirst());
                    Event.fireEvent(classUnderTest.endingNode, new ActionEvent());
                });

                fxWait(1000);
            });
        }
    }

    @Nested
    class Delete {

        @Test
        void removeEndingNode() {
            VBox parent = new VBox();
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.listElementContainer.getChildren().add(parent);
            });
            fxWait(1000);

            classUnderTest.delete("", parent);

            assertEquals(0, classUnderTest.listElementContainer.getChildren().size());
            verify(nodeAdministratorMock, times(1)).removeEndingNode("");
        }
    }

    @Nested
    class SetStartingNode {

        @Test
        void addStartingNode() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            classUnderTest.setStartingNode(OBSERVABLE_NODES.getFirst());

            assertEquals(OBSERVABLE_NODES.getFirst(), classUnderTest.startingNode.getValue());
            verify(nodeAdministratorMock, times(1)).setStartingNode(OBSERVABLE_NODES.getFirst());
        }
    }

    @Nested
    class OnNodeRemoved {

        @Test
        void removeNodes() {
            HBox firstBox =  new HBox();
            firstBox.getChildren().add(new Label("First"));
            HBox secondBox = new HBox();
            secondBox.getChildren().add(new Label("Second"));
            VBox thirdBox = new VBox();
            thirdBox.getChildren().add(new Label("Third"));
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.listElementContainer.getChildren().add(firstBox);
                classUnderTest.listElementContainer.getChildren().add(secondBox);
                classUnderTest.listElementContainer.getChildren().add(thirdBox);
            });
            fxWait(1000);

            Platform.runLater(() -> classUnderTest.onNodeRemoved(new EndingNodeRemovedEvent("Second")));

            fxWait(1000);
            assertEquals(2, classUnderTest.listElementContainer.getChildren().size());
            assertTrue(classUnderTest.listElementContainer.getChildren().contains(firstBox));
            assertTrue(classUnderTest.listElementContainer.getChildren().contains(thirdBox));
        }
    }
}
