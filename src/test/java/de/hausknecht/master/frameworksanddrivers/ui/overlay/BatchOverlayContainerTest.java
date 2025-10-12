package de.hausknecht.master.frameworksanddrivers.ui.overlay;

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatchOverlayContainerTest extends UITest {

    private final BatchOverlayContainer classUnderTest = new BatchOverlayContainer();

    @BeforeEach
    void setUp() {
        classUnderTest.batchOverlay = new StackPane();
        classUnderTest.batchImage = new ImageView();
        classUnderTest.batchContentBox = new VBox();
    }

    @Nested
    class Initialize{

        @Test
        void checkInitialValues() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            assertFalse(classUnderTest.batchOverlay.isVisible());
            assertEquals(0, classUnderTest.batchOverlay.getOpacity());
        }
    }
}