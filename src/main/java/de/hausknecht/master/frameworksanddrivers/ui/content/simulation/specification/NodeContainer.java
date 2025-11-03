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

import de.hausknecht.master.usecase.NodeAdministrator;
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
    @FXML VBox listElementContainer;
    @FXML Button addButton;
    @FXML TextField nameField;

    private final NodeAdministrator nodeAdministrator;

    final Map<String, Parent> items = new HashMap<>();

    @FXML
    void initialize(){
        addButton.setOnAction(ignored -> addListItem(nameField.getText()));
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
