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

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static de.hausknecht.master.ConstantProvider.ANIMATION_DURATIONS;
import static de.hausknecht.master.ConstantProvider.TRANSITION_DURATIONS;

@Component
@RequiredArgsConstructor
public class BatchOverlayContainer {
    static final double ANIMATION_SCALE_FROM = 0.25;
    static final double ANIMATION_SCALE_TO = 1.0;
    static final double ANIMATION_FADE_FROM = 0.0;
    static final double ANIMATION_FADE_TO = 1.0;
    static final double INITIAL_OPACITY = 0;

    @FXML StackPane batchOverlay;
    @FXML ImageView batchImage;
    @FXML VBox batchContentBox;

    private final PauseTransition pauseTransition = new PauseTransition(ANIMATION_DURATIONS);
    private Animation scale;

    @FXML
    public void initialize() {
        batchOverlay.setVisible(false);
        batchOverlay.setOpacity(0);
    }

    public void show(String imagePath) {
        batchImage.setImage(new Image(imagePath, true));
        scaleUp();
    }

    private void scaleUp() {
        if (scale != null) scale.stop();

        scaleUpSetPreconditions();
        FadeTransition fadeTransition = createFadeTransition();
        ScaleTransition scaleTransition = createScaleTransition();
        configureParallelTransition(fadeTransition, scaleTransition);
        startAnimation();
    }

    private void scaleUpSetPreconditions() {
        batchOverlay.setVisible(true);
        batchOverlay.setOpacity(INITIAL_OPACITY);
        batchContentBox.setScaleX(ANIMATION_SCALE_FROM);
        batchContentBox.setScaleY(ANIMATION_SCALE_FROM);
    }

    private FadeTransition createFadeTransition() {
        FadeTransition fadeTransition = new FadeTransition(TRANSITION_DURATIONS, batchOverlay);
        fadeTransition.setFromValue(ANIMATION_FADE_FROM);
        fadeTransition.setToValue(ANIMATION_FADE_TO);
        return fadeTransition;
    }

    private ScaleTransition createScaleTransition() {
        ScaleTransition scaleTransition = new ScaleTransition(TRANSITION_DURATIONS, batchContentBox);
        scaleTransition.setFromX(ANIMATION_SCALE_FROM);
        scaleTransition.setFromY(ANIMATION_SCALE_FROM);
        scaleTransition.setToX(ANIMATION_SCALE_TO);
        scaleTransition.setToY(ANIMATION_SCALE_TO);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        return scaleTransition;
    }

    private void configureParallelTransition(FadeTransition fadeTransition, ScaleTransition scaleTransition) {
        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);
        scale = parallelTransition;
    }

    private void startAnimation() {
        scale.setOnFinished(ignored -> {
            pauseTransition.stop();
            pauseTransition.setOnFinished(ignored2 -> hide());
            pauseTransition.playFromStart();
        });
        scale.playFromStart();
    }

    private void hide() {
        batchOverlay.setVisible(false);
    }
}
