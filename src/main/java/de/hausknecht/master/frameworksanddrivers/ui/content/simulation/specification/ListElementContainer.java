package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public interface ListElementContainer {
    void delete(String elementName, Parent listElementParent);

    default void addListItemToUI(VBox listElementContainer, String name) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/content/simulation/specification/listElements.fxml"));
        HBox elementView;
        try {
            elementView = loader.load();
            listElementContainer.getChildren().add(elementView);
        } catch (IOException e) {
            System.err.println("Failed to load list element");
        }

        ListElement elementController = loader.getController();
        elementController.setListElementContainer(this);
        elementController.setListName(name);
    }
}
