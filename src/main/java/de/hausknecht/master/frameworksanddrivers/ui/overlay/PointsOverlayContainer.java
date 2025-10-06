package de.hausknecht.master.frameworksanddrivers.ui.overlay;

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointsOverlayContainer {
    private final PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));

    @FXML private StackPane overlayContainer;
    @FXML private Label overlayMessage;

    private Animation slideIn;

    @FXML
    public void initialize() {
        overlayMessage.setVisible(false);

        overlayMessage.visibleProperty().addListener((_, _, newValue) -> {
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
        overlayMessage.setOpacity(0.0);
        overlayMessage.setTranslateX(distance);
        return distance;
    }

    private TranslateTransition createTranslateTransition(double distance) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), overlayMessage);
        transition.setFromX(distance);
        transition.setToX(0);
        return transition;
    }

    private FadeTransition createFadeTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(250), overlayMessage);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
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
            pauseTransition.setOnFinished(_ -> hide());
            pauseTransition.playFromStart();
        });
    }

    public void show(String message, boolean successful) {
        overlayMessage.getStyleClass().removeAll("success", "failure");
        overlayMessage.setText(message);
        overlayMessage.setVisible(true);
        overlayMessage.getStyleClass().add(successful ? "success" : "failure");
    }

    public void hide() {
        overlayMessage.setVisible(false);
    }
}
