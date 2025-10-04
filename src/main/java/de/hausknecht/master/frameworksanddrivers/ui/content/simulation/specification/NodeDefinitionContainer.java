package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemoved;
import de.hausknecht.master.interfaceadapters.NodeAdministrator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NodeDefinitionContainer implements ListElementContainer {
    @FXML private VBox listElementContainer;
    @FXML private Button addButton;
    @FXML private ComboBox<String> startingNode;
    @FXML private ComboBox<String> endingNode;

    private final NodeAdministrator nodeAdministrator;

    @FXML
    public void initialize(){
        startingNode.setOnAction(_ -> nodeAdministrator.setStartingNode(startingNode.getValue()));
        startingNode.setItems(nodeAdministrator.getNodes());
        endingNode.setItems(nodeAdministrator.getNodes());
        addButton.setOnAction(_ -> addListItem(endingNode.getValue()));
    }

    public void addListItem(String name) {
        if (!nodeAdministrator.addEndingNode(name)) return;
        addListItemToUI(listElementContainer, name);
    }

    public void delete(String nodeName, Parent listItem) {
        nodeAdministrator.removeEndingNode(nodeName);
        listElementContainer.getChildren().remove(listItem);
    }

    public void setStartingNode(String nodeName) {
        startingNode.setValue(nodeName);
        nodeAdministrator.setStartingNode(nodeName);
    }

    @EventListener
    public void onNodeRemoved(EndingNodeRemoved event) {
        javafx.application.Platform.runLater(() ->{
            List<HBox> list = listElementContainer.getChildren().stream()
                    .filter(HBox.class::isInstance)
                    .map(HBox.class::cast)
                    .filter(node -> node.getChildren().stream()
                            .anyMatch(label -> label instanceof Label lbl &&
                                    Objects.equals(lbl.getText(), event.name())))
                    .toList();

            listElementContainer.getChildren().removeAll(list);
        });
    }
}
