package de.hausknecht.master.frameworksanddrivers.ui.menu;

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

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuBarController {
    @FXML VBox menuBar;
    @FXML Button openMenuButton;

    private final MenuOverlay menuOverlay;

    @FXML
    public void initialize(){
        menuBar.setMaxWidth(Region.USE_PREF_SIZE);
        menuBar.setMaxHeight(Double.MAX_VALUE);
        menuBar.setPadding(new Insets(30, 20, 20, 20));

        openMenuButton.setOnAction(ignored -> menuOverlay.openMenu());
    }
}
