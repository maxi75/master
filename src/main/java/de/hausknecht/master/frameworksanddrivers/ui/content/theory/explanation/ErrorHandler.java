package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

    public void showError(String cssID, String message, VBox theoryContainer) {
        System.out.println(message);

        VBox exceptionContainer = new VBox();
        exceptionContainer.getStyleClass().add("exception-container");

        Label exception = new Label(message);
        exception.getStyleClass().add(cssID);
        exception.setWrapText(true);

        exceptionContainer.getChildren().add(exception);
        theoryContainer.getChildren().add(exceptionContainer);
    }
}
