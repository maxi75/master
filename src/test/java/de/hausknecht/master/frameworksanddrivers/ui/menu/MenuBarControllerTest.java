package de.hausknecht.master.frameworksanddrivers.ui.menu;

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuBarControllerTest extends UITest {

    private final MenuOverlay menuOverlayMock = mock();
    private final MenuBarController classUnderTest = new MenuBarController(menuOverlayMock);

    @BeforeEach
    void setUp() {
        classUnderTest.menuBar = new VBox();
        classUnderTest.openMenuButton = new Button();
    }

    @Nested
    class Initialize {

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertEquals(Region.USE_PREF_SIZE,  classUnderTest.menuBar.getMaxWidth());
            assertEquals(Double.MAX_VALUE,  classUnderTest.menuBar.getMaxHeight());
            assertEquals(20,  classUnderTest.menuBar.getPadding().getLeft());
            assertEquals(20,  classUnderTest.menuBar.getPadding().getRight());
            assertEquals(30,  classUnderTest.menuBar.getPadding().getTop());
            assertEquals(20,  classUnderTest.menuBar.getPadding().getBottom());
        }

        @Test
        void checkOpenMenuButton() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.openMenuButton.fire();
            });
            fxWait(1000);

            verify(menuOverlayMock, times(1)).openMenu();
        }
    }
}