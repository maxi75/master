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

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimulatorSpecificationContainer {

    static final double COLLAPSED = 24.0;
    static final double EXPANDED  = 400.0;
    static final String ARROW_DOWN = "▼";
    static final String ARROW_UP  = "▲";
    @FXML VBox specificationContainer;
    @FXML HBox specificationContent;
    @FXML Button specificationTabBtn;

    boolean expanded = false;

    @FXML
    void initialize() {
        specificationContainer.setPrefHeight(COLLAPSED);
        prepareContainerForCollapsing();
        specificationTabBtn.setOnAction(ignored -> toggle());
    }

    private void toggle() {

        if (!expanded) prepareContainerForExpanding();
        if (expanded) prepareContainerForCollapsing();

        playAnimation();
        expanded = !expanded;
    }

    private void prepareContainerForExpanding() {
        toggleContainer(true);
    }

    private void prepareContainerForCollapsing() {
        toggleContainer(false);
    }

    private void toggleContainer(boolean collapsed) {
        specificationContent.setVisible(collapsed);
        specificationContent.setManaged(collapsed);
        specificationTabBtn.setText(collapsed ? ARROW_DOWN : ARROW_UP);
    }

    private void playAnimation()
    {
        double from = specificationContainer.getPrefHeight();
        double to   = expanded ? COLLAPSED : EXPANDED;

        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(specificationContainer.prefHeightProperty(), from, Interpolator.EASE_BOTH));
        KeyFrame end = new KeyFrame(Duration.millis(200),
                        new KeyValue(specificationContainer.prefHeightProperty(), to, Interpolator.EASE_BOTH));

        new Timeline(start, end).play();
    }
}
