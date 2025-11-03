package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

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

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {
    static final String CSS_PAGE_EXCEPTION_CONTAINER = "exception-container";

    public void showError(String cssID, String message, VBox theoryContainer) {
        System.out.println(message);

        VBox exceptionContainer = new VBox();
        exceptionContainer.getStyleClass().add(CSS_PAGE_EXCEPTION_CONTAINER);

        Label exception = new Label(message);
        exception.getStyleClass().add(cssID);
        exception.setWrapText(true);

        exceptionContainer.getChildren().add(exception);
        theoryContainer.getChildren().add(exceptionContainer);
    }
}
