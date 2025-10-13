package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.simulationOverlay;

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.GraphAdministrator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static de.hausknecht.master.ConstantProvider.EMPTY_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimulationOverlayTest extends UITest {

    private final ApplicationEventPublisher applicationEventPublisherMock = mock();
    private final GraphAdministrator graphAdministratorMock = mock();
    private final SimulationOverlay classUnderTest = new SimulationOverlay(applicationEventPublisherMock, graphAdministratorMock);

    @BeforeEach
    void setUp() {
        classUnderTest.word = new TextField();
        classUnderTest.startBtn = new Button();
        classUnderTest.fastBackwordBtn = new Button();
        classUnderTest.backwardBtn = new Button();
        classUnderTest.forwardBtn = new Button();
        classUnderTest.fastForwardBtn = new Button();
        classUnderTest.stopBtn = new Button();
        classUnderTest.graphBox = new ComboBox<>();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);

            fxWait(1000);
            assertThat(classUnderTest.graphBox.getItems()).containsAll(Arrays.asList(AutomataSimulation.values()));
            assertEquals(AutomataSimulation.NEA, classUnderTest.graphBox.getValue());
        }

        @Test
        void textChangeResultsInResettingOfAlreadySimulatedWord() {
            Platform.runLater(classUnderTest::initialize);

            fxWait(1000);
            classUnderTest.alreadySimulatedWord = "ABC";

            Platform.runLater(() -> classUnderTest.word.textProperty().set("change"));
            fxWait(250);
            assertEquals(EMPTY_STRING, classUnderTest.alreadySimulatedWord);
        }

        @Test
        void checkForwardButtonActionNotStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.forwardBtn.fire();
            });

            fxWait(1000);
            assertTrue(classUnderTest.alreadyStarted);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void checkForwardButtonActionAlreadyStartedWithoutNext() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.forwardBtn.fire();
            });

            fxWait(1000);
            assertTrue(classUnderTest.alreadyStarted);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void checkForwardButtonActionAlreadyStartedWithNext() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.word.setText("ABC");
                classUnderTest.forwardBtn.fire();
            });

            fxWait(1000);
            assertTrue(classUnderTest.alreadyStarted);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent("ABC "));
        }

        @Test
        void simulateBackWithoutAlreadyStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.backwardBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateBackWithAlreadyStartedButNoLastInput() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.backwardBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateBackWithAlreadyStartedWithLastInput() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.alreadySimulatedWord = "ABC DEF XYZ";
                classUnderTest.backwardBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent("ABC DEF "));
        }

        @Test
        void simulateStartButtonWithoutAlreadyStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.startBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateStartButtonWithAlreadyStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.startBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateFastBackwardButtonWithoutAlreadyStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.fastBackwordBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(0)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateFastBackwardButtonWithAlreadyStarted() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.fastBackwordBtn.fire();
            });

            fxWait(1000);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent(EMPTY_STRING));
        }

        @Test
        void simulateFastForwardButton() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.word.setText("ABC DEF XYZ");
                classUnderTest.fastForwardBtn.fire();
            });

            fxWait(1000);
            assertTrue(classUnderTest.alreadyStarted);
            assertEquals("ABC DEF XYZ", classUnderTest.word.getText());
            verify(applicationEventPublisherMock, times(1)).publishEvent(new SimulationEvent("ABC DEF XYZ"));
        }

        @Test
        void simulateStopButton() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.alreadyStarted = true;
                classUnderTest.stopBtn.fire();
            });

            fxWait(1000);
            assertFalse(classUnderTest.alreadyStarted);
            assertEquals(EMPTY_STRING, classUnderTest.alreadySimulatedWord);
            verify(applicationEventPublisherMock, times(1)).publishEvent(new GraphChangedEvent());
        }
    }

    @Test
    void setComboBox() {
        Platform.runLater(classUnderTest::initialize);
        fxWait(1000);

        classUnderTest.setComboBox(AutomataSimulation.NEA);

        assertEquals(AutomataSimulation.NEA, classUnderTest.graphBox.getValue());
        verify(graphAdministratorMock, times(1)).changeSelectedGraph(AutomataSimulation.NEA);
    }
}