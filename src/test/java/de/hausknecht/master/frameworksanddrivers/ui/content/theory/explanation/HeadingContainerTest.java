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
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeadingContainerTest extends UITest {

    private final HeadingContainer classUnderTest = new HeadingContainer();

    @Nested
    class AddHeadingToTheoryContainer {

        @Test
        void headingNull() {
            TheoryPageData theoryPageData = TestDataGenerator.getCorrectTheoryPageData();
            theoryPageData.setMainHeading(null);
            VBox container = new VBox();

            classUnderTest.addHeadingToTheoryContainer(theoryPageData, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void headingBlank() {
            TheoryPageData theoryPageData = TestDataGenerator.getCorrectTheoryPageData();
            theoryPageData.setMainHeading(" ");
            VBox container = new VBox();

            classUnderTest.addHeadingToTheoryContainer(theoryPageData, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void headingAvailable() {
            TheoryPageData theoryPageData = TestDataGenerator.getCorrectTheoryPageData();
            theoryPageData.setMainHeading("HEADING");
            VBox container = new VBox();

            classUnderTest.addHeadingToTheoryContainer(theoryPageData, container);

            assertFalse(container.getChildren().isEmpty());
        }
    }

    @Nested
    class AddSubheadingToTheoryContainer {

        @Test
        void subheadingNull() {
            TheoryPageData.Section theoryPageDataSection = TestDataGenerator.getCorrectTheoryPageDataSection();
            theoryPageDataSection.setHeading(null);
            VBox container = new VBox();

            classUnderTest.addSubHeading(theoryPageDataSection, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void subheadingBlank() {
            TheoryPageData.Section theoryPageDataSection = TestDataGenerator.getCorrectTheoryPageDataSection();
            theoryPageDataSection.setHeading(" ");
            VBox container = new VBox();

            classUnderTest.addSubHeading(theoryPageDataSection, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void subheadingAvailable() {
            TheoryPageData.Section theoryPageDataSection = TestDataGenerator.getCorrectTheoryPageDataSection();
            theoryPageDataSection.setHeading("HEADING");
            VBox container = new VBox();

            classUnderTest.addSubHeading(theoryPageDataSection, container);

            assertFalse(container.getChildren().isEmpty());
        }
    }
}
