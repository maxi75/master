package de.hausknecht.master.frameworksanddrivers.ui.batch;

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
import de.hausknecht.master.entity.domain.eventdata.UpdatedTimeEvent;
import de.hausknecht.master.frameworksanddrivers.ui.overlay.BatchOverlayContainer;
import de.hausknecht.master.usecase.PointSystemAdministrator;
import de.hausknecht.master.usecase.SessionTimeAccessor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static de.hausknecht.master.ConstantProvider.*;

@Component
@RequiredArgsConstructor
public class BatchContainer {
    @FXML HBox batchPage;
    @FXML ImageView timeNovice;
    @FXML ImageView timeIntermediate;
    @FXML ImageView timeMaster;
    @FXML ImageView timeKing;
    @FXML ImageView exerciseNovice;
    @FXML ImageView exerciseIntermediate;
    @FXML ImageView exerciseMaster;
    @FXML ImageView exerciseKing;
    @FXML ImageView legend;

    private final SessionTimeAccessor sessionTimeAccessor;
    private final BatchOverlayContainer batchOverlayContainer;
    private final PointSystemAdministrator pointSystemAdministrator;

    @FXML
    void initialize(){
        batchPage.setVisible(false);

        Duration duration = sessionTimeAccessor.getSessionTime();
        int points = pointSystemAdministrator.getPoints();

        Platform.runLater(() -> updateImages(duration, false));
        Platform.runLater(() -> updateImages(points, false));
    }

    public void hide() {
        Platform.runLater(() -> batchPage.setVisible(false));
    }

    public void show() {
        Platform.runLater(() -> batchPage.setVisible(true));
    }

    @EventListener
    public void onTimerUpdated(UpdatedTimeEvent ignored) {
        javafx.application.Platform.runLater(() ->{
            Duration duration = sessionTimeAccessor.getSessionTime();
            updateImages(duration, true);
        });
    }

    @EventListener
    public void onPointsChanged(PointsChangedEvent ignored) {
        javafx.application.Platform.runLater(() ->{
            int points = pointSystemAdministrator.getPoints();
            updateImages(points, true);
        });
    }

    private void updateImages(Duration duration, boolean withAnimation) {
        if (duration.greaterThan(Duration.minutes(5)) && !timeNovice.getImage().getUrl().contains(BATCH_TIME_NOVICE_PATH)) {
            timeNovice.setImage(new Image(BATCH_TIME_NOVICE_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_TIME_NOVICE_PATH);
        }
        if (duration.greaterThan(Duration.minutes(15)) && !timeIntermediate.getImage().getUrl().contains(BATCH_TIME_INTERMEDIATE_PATH)) {
            timeIntermediate.setImage(new Image(BATCH_TIME_INTERMEDIATE_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_TIME_INTERMEDIATE_PATH);
        }
        if (duration.greaterThan(Duration.minutes(90)) && !timeMaster.getImage().getUrl().contains(BATCH_TIME_MASTER_PATH)) {
            timeMaster.setImage(new Image(BATCH_TIME_MASTER_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_TIME_MASTER_PATH);
        }
        if (duration.greaterThan(Duration.minutes(300)) && !timeKing.getImage().getUrl().contains(BATCH_TIME_KING_PATH)) {
            timeKing.setImage(new Image(BATCH_TIME_KING_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_TIME_KING_PATH);
        }

        showLegendIfNecessary(withAnimation);
    }
    private void updateImages(int points, boolean withAnimation) {
        if (points >= 25 && !exerciseNovice.getImage().getUrl().contains(BATCH_EXERCISE_NOVICE_PATH)) {
            exerciseNovice.setImage(new Image(BATCH_EXERCISE_NOVICE_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_EXERCISE_NOVICE_PATH);
        }
        if (points >= 75 && !exerciseIntermediate.getImage().getUrl().contains(BATCH_EXERCISE_INTERMEDIATE_PATH)) {
            exerciseIntermediate.setImage(new Image(BATCH_EXERCISE_INTERMEDIATE_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_EXERCISE_INTERMEDIATE_PATH);
        }
        if (points >= 150 && !exerciseMaster.getImage().getUrl().contains(BATCH_EXERCISE_MASTER_PATH)) {
            exerciseMaster.setImage(new Image(BATCH_EXERCISE_MASTER_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_EXERCISE_MASTER_PATH);
        }
        if (points >= 450 && !exerciseKing.getImage().getUrl().contains(BATCH_EXERCISE_KING_PATH)) {
            exerciseKing.setImage(new Image(BATCH_EXERCISE_KING_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_EXERCISE_KING_PATH);
        }

        showLegendIfNecessary(withAnimation);
    }

    private void showLegendIfNecessary(boolean withAnimation) {
        if (timeKing.getImage().getUrl().contains(BATCH_TIME_KING_PATH) &&
                exerciseKing.getImage().getUrl().contains(BATCH_EXERCISE_KING_PATH) &&
                legend.getImage().getUrl().contains(BATCH_LEGEND_WITHOUT_SATURATION_PATH)) {

            legend.setImage(new Image(BATCH_LEGEND_PATH, true));
            if (withAnimation) batchOverlayContainer.show(BATCH_LEGEND_PATH);
        }
    }
}
