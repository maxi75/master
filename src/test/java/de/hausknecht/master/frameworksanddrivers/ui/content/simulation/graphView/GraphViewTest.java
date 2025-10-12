package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.graphView;

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.GraphAdministrator;
import javafx.application.Platform;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.WebView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GraphViewTest extends UITest {

    private final GraphAdministrator graphAdministratorMock = mock();

    private final GraphView classUnderTest = new GraphView(graphAdministratorMock);

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