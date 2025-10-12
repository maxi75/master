package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListElementTest extends UITest {
    private static final String LABEL_TEXT = "TEXT";
    private static final String NEW_TEXT = "NEW TEXT";

    private final ListElementContainer listElementContainerMock = mock();
    private final ListElement classUnderTest = new ListElement();

    @BeforeEach
    void setUp() {
        classUnderTest.setListElementContainer(listElementContainerMock);
        classUnderTest.listElementName = new Label(LABEL_TEXT);
        classUnderTest.deleteBtn = new Button();
    }

    @Nested
    class Initialize {

        @Test
        void initialize() {
            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.deleteBtn.fire();
            });
            fxWait(1000);

            verify(listElementContainerMock, times(1)).delete(LABEL_TEXT, null);
        }
    }

    @Nested
    class SetListName {

        @Test
        void setListName() {
            Platform.runLater(classUnderTest::initialize);
            fxWait(1000);

            classUnderTest.setListName(NEW_TEXT);

            assertEquals(NEW_TEXT, classUnderTest.listElementName.getText());
        }
    }
}