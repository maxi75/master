package de.hausknecht.master.frameworksanddrivers.ui.overlay;

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
