package de.hausknecht.master.frameworksanddrivers.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TransitionContainer {
    @FXML private VBox listElementContainer;
    @FXML private Button addButton;
    @FXML private TextField nameField;

    private final ArrayList<String> registeredNodes = new ArrayList<>();

    @FXML
    public void initialize(){
        addButton.setOnAction(e -> addListItem(nameField.getText()));
    }

    private void addListItem(String name) {
        if (name == null || name.isBlank() || registeredNodes.contains(name))
            return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/content/simulation/listElements.fxml"));
        HBox elementView;
        try {
            elementView = loader.load();
            listElementContainer.getChildren().add(elementView);
            registeredNodes.add(name);
        } catch (IOException e) {
            System.err.println("Failed to load list element");
        }

        ListElement elementController = loader.getController();
        elementController.setNodeContainer(this);
        elementController.setListName(name);
    }

    public void delete(String nodeName, Parent listItem) {
        registeredNodes.remove(nodeName);
        listElementContainer.getChildren().remove(listItem);
    }
}
