package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

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

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemovedEvent;
import de.hausknecht.master.usecase.NodeAdministrator;
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
    @FXML VBox listElementContainer;
    @FXML Button addButton;
    @FXML ComboBox<String> startingNode;
    @FXML ComboBox<String> endingNode;

    private final NodeAdministrator nodeAdministrator;

    @FXML
    void initialize(){
        startingNode.setOnAction(ignored -> nodeAdministrator.setStartingNode(startingNode.getValue()));
        startingNode.setItems(nodeAdministrator.getNodes());
        endingNode.setItems(nodeAdministrator.getNodes());
        addButton.setOnAction(ignored -> addListItem(endingNode.getValue()));
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
    public void onNodeRemoved(EndingNodeRemovedEvent event) {
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
