package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

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
    @FXML private VBox specificationContainer;
    @FXML private HBox specificationContent;
    @FXML private Button specificationTabBtn;

    private boolean expanded = false;

    @FXML
    private void initialize() {
        specificationContainer.setPrefHeight(COLLAPSED);
        prepareContainerForCollapsing();
        specificationTabBtn.setOnAction(_ -> toggle());
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
