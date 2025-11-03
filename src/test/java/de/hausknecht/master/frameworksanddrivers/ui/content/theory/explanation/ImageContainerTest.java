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

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageContainerTest extends UITest {

    private final ClasspathData classpathDataMock = mock();
    private final ErrorHandler errorHandlerMock = mock();

    private final ImageContainer classUnderTest = new ImageContainer(classpathDataMock, errorHandlerMock);

    @BeforeEach
    void setUp() {
        when(classpathDataMock.loadImage(anyString())).thenReturn("icon.png");
    }

    @Nested
    class AddImages {

        @Test
        void getImageNull() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setImage(null);
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(classpathDataMock, times(0)).loadImage(anyString());
        }

        @Test
        void getImageBlank() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setImage(" ");
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(classpathDataMock, times(0)).loadImage(anyString());
        }

        @Test
        void imageStringIsNull() {
            when(classpathDataMock.loadImage(anyString())).thenReturn(null);
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            verify(errorHandlerMock, times(1)).showError(anyString(), anyString(), any());
        }

        @Test
        void imageIsValid() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            VBox container = new VBox();

            classUnderTest.addImages(section, container);

            assertFalse(container.getChildren().isEmpty());
        }
    }
}
