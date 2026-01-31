package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.graphView;

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

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleTheoryEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.GraphAdministrator;
import javafx.application.Platform;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static de.hausknecht.master.ConstantProvider.EMPTY_STRING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GraphViewTest extends UITest {

    private final GraphAdministrator graphAdministratorMock = mock();
    private final ApplicationEventPublisher applicationEventPublisherMock = mock();

    private final GraphView classUnderTest = new GraphView(graphAdministratorMock, applicationEventPublisherMock);

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> classUnderTest.graphView = new WebView());

        when(graphAdministratorMock.returnGraphDefinition()).thenReturn(Optional.of("diagraph { a -> b }"));
        when(graphAdministratorMock.returnSimulatedGraphDefinition(anyString())).thenReturn(Optional.of("digraph { a }"));
    }

    @Nested
    class Initialize {

        @Test
        void initializesGraph() {
            Platform.runLater(() -> {
                classUnderTest.graphView.setZoom(0);

                classUnderTest.initialize();
            });

            fxWait(1000);
            assertEquals(1, classUnderTest.graphView.getZoom());
        }

        @Test
        void onScrollWithPressingControlDown() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                assertEquals(1, classUnderTest.graphView.getZoom());

                ScrollEvent scrollEvent = getScrollEvent(true, 1);
                ScrollEvent.fireEvent(classUnderTest.graphView, scrollEvent);
            });

            fxWait(1000);
            assertNotEquals(1, classUnderTest.graphView.getZoom());
        }

        @Test
        void onScrolledLessThanPossible() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                assertEquals(1, classUnderTest.graphView.getZoom());

                for (int i = 0; i < 10; i++) {
                    ScrollEvent scrollEvent = getScrollEvent(true, -1);
                    ScrollEvent.fireEvent(classUnderTest.graphView, scrollEvent);
                }
            });

            fxWait(1000);
            assertEquals(0.2, classUnderTest.graphView.getZoom());
        }

        @Test
        void onScrollWithoutPressingControlDown() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                assertEquals(1, classUnderTest.graphView.getZoom());

                ScrollEvent scrollEvent = getScrollEvent(false, 1);
                ScrollEvent.fireEvent(classUnderTest.graphView, scrollEvent);
            });

            fxWait(1000);
            assertEquals(1, classUnderTest.graphView.getZoom());
        }

        @Test
        void onScrolledMoreThanPossible() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                assertEquals(1, classUnderTest.graphView.getZoom());

                for (int i = 0; i < 41; i++) {
                    ScrollEvent scrollEvent = getScrollEvent(true, 1);
                    ScrollEvent.fireEvent(classUnderTest.graphView, scrollEvent);
                }
            });

            fxWait(1000);
            assertEquals(5, classUnderTest.graphView.getZoom());
        }

        private ScrollEvent getScrollEvent(boolean pressed, int scrollValue) {
            return new ScrollEvent(
                    ScrollEvent.SCROLL,
                    scrollValue, scrollValue,
                    scrollValue, scrollValue,
                    false, pressed,
                    false, false,
                    false, false,
                    scrollValue, scrollValue,
                    scrollValue, scrollValue,
                    scrollValue, scrollValue,
                    ScrollEvent.HorizontalTextScrollUnits.NONE, scrollValue,
                    ScrollEvent.VerticalTextScrollUnits.NONE, scrollValue,
                    scrollValue, null
            );
        }
    }

    @Nested
    class OnFullsizeClicked {

        @Test
        void checkEventPublication() {
            Platform.runLater(() -> {
                classUnderTest.toggleTheory();

                ArgumentCaptor<ToggleTheoryEvent> captor = ArgumentCaptor.forClass(ToggleTheoryEvent.class);
                verify(applicationEventPublisherMock, times(1)).publishEvent(captor.capture());
                assertNotNull(captor.getValue());
                assertEquals("Fullsize", captor.getValue().name());
            });
        }

        @Test
        void checkIconToggle() {
            Platform.runLater(() -> {
                assertEquals("⤢", classUnderTest.fullsize.getText());

                classUnderTest.toggleTheory();
                assertEquals("⤡", classUnderTest.fullsize.getText());

                classUnderTest.toggleTheory();
                assertEquals("⤢", classUnderTest.fullsize.getText());
            });
        }
    }

    @Nested
    class OnGraphChanged {

        @Test
        void graphDefinitionWillBeCalled() {
            Platform.runLater(() -> classUnderTest.onGraphChanged(new GraphChangedEvent()));

            fxWait(1000);
            verify(graphAdministratorMock, times(1)).returnGraphDefinition();
        }
    }

    @Nested
    class simulatedGraph {

        @Test
        void graphDefinitionWillBeCalled() {
            Platform.runLater(() -> classUnderTest.simulatedGraph(new SimulationEvent("input")));

            fxWait(1500);
            verify(graphAdministratorMock, times(1)).returnSimulatedGraphDefinition(eq("input"));
        }
    }
}
