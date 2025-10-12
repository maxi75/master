package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

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