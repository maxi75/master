package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.interfaceadapters.NodeAdministrator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NodeContainer implements ListElementContainer {
    @FXML private VBox listElementContainer;
    @FXML private Button addButton;
    @FXML private TextField nameField;

    private final NodeAdministrator nodeAdministrator;

    private final Map<String, Parent> items = new HashMap<>();

    @FXML
    public void initialize(){
        addButton.setOnAction(_ -> addListItem(nameField.getText()));
    }

    public void addListItem(String name) {
        if (!nodeAdministrator.addNode(name)) return;
        Parent item = addListItemToUI(listElementContainer, name);
        items.put(name, item);
    }

    public void deleteAll() {
        items.forEach(this::delete);
    }

    public void delete(String nodeName, Parent listItem) {
        nodeAdministrator.removeNodeFromNodeRegistry(nodeName);
        listElementContainer.getChildren().remove(listItem);
    }
}
