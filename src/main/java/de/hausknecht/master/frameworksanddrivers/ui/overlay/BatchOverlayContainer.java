package de.hausknecht.master.frameworksanddrivers.ui.overlay;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchOverlayContainer {
    private final PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));

    @FXML private StackPane batchOverlay;
    @FXML private ImageView batchImage;
    @FXML private VBox batchContentBox;

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
        batchOverlay.setOpacity(0);
        batchContentBox.setScaleX(0.25);
        batchContentBox.setScaleY(0.25);
    }

    private FadeTransition createFadeTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), batchOverlay);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        return fadeTransition;
    }

    private ScaleTransition createScaleTransition() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), batchContentBox);
        scaleTransition.setFromX(0.25);
        scaleTransition.setFromY(0.25);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        return scaleTransition;
    }

    private void configureParallelTransition(FadeTransition fadeTransition, ScaleTransition scaleTransition) {
        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.setInterpolator(Interpolator.EASE_OUT);
        scale = parallelTransition;
    }

    private void startAnimation() {
        scale.setOnFinished(_ -> {
            pauseTransition.stop();
            pauseTransition.setOnFinished(_ -> hide());
            pauseTransition.playFromStart();
        });
        scale.playFromStart();
    }

    private void hide() {
        batchOverlay.setVisible(false);
    }
}
