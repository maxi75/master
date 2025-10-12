package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.frameworksanddrivers.ui.UITest;
import de.hausknecht.master.usecase.NodeAdministrator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NodeContainerTest extends UITest {
    private static final String TEXT = "TEXT";

    private final NodeAdministrator nodeAdministratorMock = mock();
    private final NodeContainer classUnderTest = new NodeContainer(nodeAdministratorMock);

    @BeforeEach
    void setUp(){
        when(nodeAdministratorMock.addNode(anyString())).thenReturn(true);

        classUnderTest.listElementContainer = new VBox();
        classUnderTest.addButton = new Button();
        classUnderTest.nameField = new TextField();
        classUnderTest.nameField.setText(TEXT);
    }

    @Nested
    class Initialize {

        @Test
        void addButtonWithAlreadyExistingNode() {
            when(nodeAdministratorMock.addNode(anyString())).thenReturn(false);

            Platform.runLater(() -> {
                classUnderTest.initialize();
                classUnderTest.addButton.fire();
            });

            fxWait(1000);
            assertEquals(0, classUnderTest.items.size());
        }
    }

    @Nested
    class DeleteAll {

        @Test
        void deleteAll() {
            classUnderTest.items.put(TEXT, classUnderTest.listElementContainer);

            classUnderTest.deleteAll();

            verify(nodeAdministratorMock, times(1)).removeNodeFromNodeRegistry(TEXT);
        }
    }
}