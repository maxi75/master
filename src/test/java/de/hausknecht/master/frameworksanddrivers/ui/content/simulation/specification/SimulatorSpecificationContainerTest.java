package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

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