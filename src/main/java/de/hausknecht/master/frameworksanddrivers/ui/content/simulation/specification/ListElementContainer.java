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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public interface ListElementContainer {
    void delete(String elementName, Parent listElementParent);

    default Parent addListItemToUI(VBox listElementContainer, String name) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/content/simulation/specification/ListElements.fxml"));
        try {
            Parent elementView = loader.load();

            ListElement elementController = loader.getController();
            elementController.setListElementContainer(this);
            elementController.setListName(name);
            listElementContainer.getChildren().add(elementView);
            return elementView;

        } catch (IOException e) {
            System.err.println("Failed to load list element");
            return null;
        }
    }
}
