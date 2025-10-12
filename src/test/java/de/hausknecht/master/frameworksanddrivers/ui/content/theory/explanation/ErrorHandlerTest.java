package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest extends UITest {

    private final ErrorHandler classUnderTest = new  ErrorHandler();

    @Nested
    class ShowError {

        @Test
        void showError() {
            String cssID = "ID";
            String message = "message";
            VBox container = new VBox();

            classUnderTest.showError(cssID, message, container);

            assertFalse(container.getChildren().isEmpty());
            VBox errorContainer = (VBox) container.getChildren().getFirst();
            assertFalse(errorContainer.getChildren().isEmpty());
        }
    }
}