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

import de.hausknecht.master.entity.domain.eventdata.TransitionRemovedEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.NodeAdministrator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransitionContainerTest extends UITest {
    private static final String TRANSITION_WORD = "WORD";
    private static final List<String> NODES = List.of("ABC", "DEF");
    private static final ObservableList<String> OBSERVABLE_NODES = FXCollections.observableList(NODES);

    private final NodeAdministrator nodeAdministratorMock = mock();
    private final TransitionContainer classUnderTest = new TransitionContainer(nodeAdministratorMock);

    @BeforeEach
    void setUp() {
        when(nodeAdministratorMock.getNodes()).thenReturn(OBSERVABLE_NODES);
        when(nodeAdministratorMock.addEndingNode(anyString())).thenReturn(Boolean.valueOf(true));

        classUnderTest.listElementContainer = new VBox();
        classUnderTest.addButton = new Button();
        classUnderTest.fromTransition = new ComboBox<>();
        classUnderTest.toTransition = new ComboBox<>();
        classUnderTest.transitionWord = new TextField();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertThat(classUnderTest.fromTransition.getItems()).containsAll(NODES);
            assertThat(classUnderTest.toTransition.getItems()).containsAll(NODES);
        }

        @Test
        void checkAddListItem() {
            assertThatNoException().isThrownBy(() -> {
                Platform.runLater(() -> {
                    classUnderTest.initialize();
                    classUnderTest.fromTransition.setValue(NODES.getFirst());
                    classUnderTest.toTransition.setValue(NODES.getLast());
                    classUnderTest.transitionWord.setText(TRANSITION_WORD);
                    classUnderTest.addButton.fire();
                });

                fxWait(1000);
            });
        }
    }

    @Nested
    class Delete {

        @Test
        void checkDelete() {
            classUnderTest.delete("ABC --WORD--> DEF", new VBox());

            verify(nodeAdministratorMock, times(1)).removeNodeFromTransitionRegistry(any());
        }

        @Test
        void nodeNameIsNull() {
            classUnderTest.delete(null, new VBox());

            verify(nodeAdministratorMock, times(0)).removeNodeFromTransitionRegistry(any());
        }

        @Test
        void nodeNameIsBlank() {
            classUnderTest.delete(" ", new VBox());

            verify(nodeAdministratorMock, times(0)).removeNodeFromTransitionRegistry(any());
        }

        @Test
        void listItemIsNUll() {
            classUnderTest.delete(NODES.getFirst(), null);

            verify(nodeAdministratorMock, times(0)).removeNodeFromTransitionRegistry(any());
        }

        @Test
        void patternMatchingNotPossible() {
            classUnderTest.delete(NODES.getFirst(), new TextField());

            verify(nodeAdministratorMock, times(0)).removeNodeFromTransitionRegistry(any());
        }
    }

    @Nested
    class OnNodeRemoved {

        @Test
        void removeNodes() {
            HBox firstBox =  new HBox();
            firstBox.getChildren().add(new Label("First --WORD--> DEF"));
            HBox secondBox = new HBox();
            secondBox.getChildren().add(new Label("Second --WORD--> DEF"));
            VBox thirdBox = new VBox();
            thirdBox.getChildren().add(new Label("Third --WORD--> DEF"));
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.listElementContainer.getChildren().add(firstBox);
                classUnderTest.listElementContainer.getChildren().add(secondBox);
                classUnderTest.listElementContainer.getChildren().add(thirdBox);
            });
            fxWait(1000);

            Platform.runLater(() -> classUnderTest.onNodeRemoved(new TransitionRemovedEvent("Second", "DEF", "WORD")));

            fxWait(1000);
            assertEquals(2, classUnderTest.listElementContainer.getChildren().size());
            assertTrue(classUnderTest.listElementContainer.getChildren().contains(firstBox));
            assertTrue(classUnderTest.listElementContainer.getChildren().contains(thirdBox));
        }
    }
}
