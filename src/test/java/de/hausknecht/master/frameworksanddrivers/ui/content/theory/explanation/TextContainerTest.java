package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextContainerTest extends UITest {

    private final TextContainer classUnderTest = new TextContainer();

    @Nested
    class AddText {

        @Test
        void textIsNull() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setText(null);
            VBox container = new VBox();

            classUnderTest.addText(section, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void textIsBlank() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setText(" ");
            VBox container = new VBox();

            classUnderTest.addText(section, container);

            assertTrue(container.getChildren().isEmpty());
        }

        @Test
        void testIsValid() {
            TheoryPageData.Section section = TestDataGenerator.getCorrectTheoryPageDataSection();
            section.setText("Text");
            VBox container = new VBox();

            classUnderTest.addText(section, container);

            assertFalse(container.getChildren().isEmpty());
        }
    }
}