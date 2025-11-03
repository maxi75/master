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
