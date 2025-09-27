package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.interfaceadapters.NodeAdministrator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeContainer implements ListElementContainer {
    @FXML private VBox listElementContainer;
    @FXML private Button addButton;
    @FXML private TextField nameField;

    private final NodeAdministrator nodeAdministrator;

    @FXML
    public void initialize(){
        addButton.setOnAction(_ -> addListItem(nameField.getText()));
    }

    private void addListItem(String name) {
        if (!nodeAdministrator.addNode(name)) return;
        addListItemToUI(listElementContainer, name);
    }

    public void delete(String nodeName, Parent listItem) {
        nodeAdministrator.removeNodeFromNodeRegistry(nodeName);
        listElementContainer.getChildren().remove(listItem);
    }
}
