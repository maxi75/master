package de.hausknecht.master.frameworksanddrivers.ui.overlay;

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

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static de.hausknecht.master.ConstantProvider.ANIMATION_DURATIONS;
import static de.hausknecht.master.ConstantProvider.TRANSITION_DURATIONS;

@Component
@RequiredArgsConstructor
public class PointsOverlayContainer {
    static final String CSS_CLASS_SUCCESS = "success";
    static final String CSS_CLASS_FAILURE = "failure";
    static final double ANIMATION_FADE_FROM = 0.0;
    static final double ANIMATION_FADE_TO = 1.0;
    static final double ANIMATION_TRANSLATE_TO = 0.0;
    static final double INITIAL_OPACITY = 0;

    private final PauseTransition pauseTransition = new PauseTransition(ANIMATION_DURATIONS);

    @FXML StackPane overlayContainer;
    @FXML Label overlayMessage;

    private Animation slideIn;

    @FXML
    public void initialize() {
        overlayMessage.setVisible(false);

        overlayMessage.visibleProperty().addListener((ignored, ignored2, newValue) -> {
            if (newValue) slideIn();
        });
    }

    private void slideIn() {
        if (slideIn != null) slideIn.stop();

        double distance = setPreconditions();
        TranslateTransition translate = createTranslateTransition(distance);
        FadeTransition fadeTransition = createFadeTransition();
        configureParallelTransition(translate, fadeTransition);
        slideIn.playFromStart();
    }

    private double setPreconditions() {
        double distance = overlayContainer.getWidth();
        overlayMessage.setOpacity(INITIAL_OPACITY);
        overlayMessage.setTranslateX(distance);
        return distance;
    }

    private TranslateTransition createTranslateTransition(double distance) {
        TranslateTransition transition = new TranslateTransition(TRANSITION_DURATIONS, overlayMessage);
        transition.setFromX(distance);
        transition.setToX(ANIMATION_TRANSLATE_TO);
        return transition;
    }

    private FadeTransition createFadeTransition() {
        FadeTransition fadeTransition = new FadeTransition(TRANSITION_DURATIONS, overlayMessage);
        fadeTransition.setFromValue(ANIMATION_FADE_FROM);
        fadeTransition.setToValue(ANIMATION_FADE_TO);
        return fadeTransition;
    }

    private void configureParallelTransition(TranslateTransition translateTransition, FadeTransition fadeTransition) {
        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);
        slideIn = parallelTransition;
    }

    @EventListener
    public void onPointsChanged(PointsChangedEvent event) {
        javafx.application.Platform.runLater(() -> {
            show(event.message(), event.successful());
            pauseTransition.stop();
            pauseTransition.setOnFinished(ignored -> hide());
            pauseTransition.playFromStart();
        });
    }

    public void show(String message, boolean successful) {
        overlayMessage.getStyleClass().removeAll(CSS_CLASS_SUCCESS, CSS_CLASS_FAILURE);
        overlayMessage.setText(message);
        overlayMessage.setVisible(true);
        overlayMessage.getStyleClass().add(successful ? CSS_CLASS_SUCCESS : CSS_CLASS_FAILURE);
    }

    public void hide() {
        overlayMessage.setVisible(false);
    }
}
