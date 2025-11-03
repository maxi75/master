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

import de.hausknecht.master.entity.domain.eventdata.PointsChangedEvent;
import de.hausknecht.master.frameworksanddrivers.ui.batch.BatchContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.TheoryContainer;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static de.hausknecht.master.ConstantProvider.LOCK;
import static de.hausknecht.master.ConstantProvider.POINTS_SPECIAL_EXERCISE;

@Component
@RequiredArgsConstructor
public class MenuOverlay {
    static final String POINTS = "Punktzahl: ";

    private final TheoryContainer theoryContainer;
    private final PointSystemAdministrator pointSystemAdministrator;
    private final BatchContainer batchContainer;

    @FXML VBox menuOverlay;
    @FXML Button closeButton;
    @FXML Label statusPoints;
    @FXML Button batchNavigation;
    @FXML Button specialExercise;
    @FXML ImageView lockImage;

    @FXML
    void initialize() {
        menuOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        menuOverlay.setMaxHeight(Double.MAX_VALUE);
        menuOverlay.setPadding(new Insets(30, 50, 20, 50));
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

        statusPoints.setText(POINTS + pointSystemAdministrator.getPoints());

        closeButton.setOnAction(ignored -> this.closeMenu());
        configureBatchNavigation();
        configureSpecialExercises();
        this.closeMenu();
    }

    private void configureBatchNavigation() {
        batchNavigation.setOnAction(ignored -> {
            batchContainer.show();
            closeMenu();
        });
    }

    @FXML
    private void loadContent(ActionEvent e) {
        Button menuNavigationButton = (Button) e.getSource();
        String file = (String) menuNavigationButton.getUserData();
        openMenuEntry(file);
    }

    private void openMenuEntry(String file) {
        if (file != null && !file.isBlank()) {
            theoryContainer.renderTheoryData(file);
            closeMenu();
            batchContainer.hide();
        }
    }

    @EventListener
    public void onPointsChanged(PointsChangedEvent ignored) {
        javafx.application.Platform.runLater(() -> {
            int currentPoints = pointSystemAdministrator.getPoints();
            statusPoints.setText(POINTS + currentPoints);
            configureSpecialExercises();
        });
    }

    private void configureSpecialExercises() {
        int points = pointSystemAdministrator.getPoints();
        if (points >= POINTS_SPECIAL_EXERCISE) lockImage.setImage(null);
        if (points < POINTS_SPECIAL_EXERCISE) lockImage.setImage(new Image(LOCK, true));

        specialExercise.setOnAction(ignored -> {
            String file = (String) specialExercise.getUserData();
            if (points >= POINTS_SPECIAL_EXERCISE) openMenuEntry(file);
        });
    }

    public void closeMenu() {
        menuOverlay.setVisible(false);
    }

    public void openMenu() {
        menuOverlay.setVisible(true);
    }
}
