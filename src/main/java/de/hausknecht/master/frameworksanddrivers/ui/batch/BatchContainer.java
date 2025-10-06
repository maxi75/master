package de.hausknecht.master.frameworksanddrivers.ui.batch;

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

@Component
@RequiredArgsConstructor
public class BatchContainer {
    @FXML private HBox batchPage;
    @FXML private ImageView timeNovice;
    @FXML private ImageView timeIntermediate;
    @FXML private ImageView timeMaster;
    @FXML private ImageView timeKing;
    @FXML private ImageView exerciseNovice;
    @FXML private ImageView exerciseIntermediate;
    @FXML private ImageView exerciseMaster;
    @FXML private ImageView exerciseKing;
    @FXML private ImageView legend;

    private final SessionTimeAccessor sessionTimeAccessor;
    private final BatchOverlayContainer batchOverlayContainer;
    private final PointSystemAdministrator pointSystemAdministrator;

    @FXML
    public void initialize(){
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
        if (duration.greaterThan(Duration.minutes(5)) && !timeNovice.getImage().getUrl().contains("/packaging/batches/time/TimeNovice.png")) {
            timeNovice.setImage(new Image("/packaging/batches/time/TimeNovice.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/time/TimeNovice.png");
        }
        if (duration.greaterThan(Duration.minutes(15)) && !timeIntermediate.getImage().getUrl().contains("/packaging/batches/time/TimeIntermediate.png")) {
            timeIntermediate.setImage(new Image("/packaging/batches/time/TimeIntermediate.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/time/TimeIntermediate.png");
        }
        if (duration.greaterThan(Duration.minutes(90)) && !timeMaster.getImage().getUrl().contains("/packaging/batches/time/TimeMaster.png")) {
            timeMaster.setImage(new Image("/packaging/batches/time/TimeMaster.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/time/TimeMaster.png");
        }
        if (duration.greaterThan(Duration.minutes(300)) && !timeKing.getImage().getUrl().contains("/packaging/batches/time/TimeKing.png")) {
            timeKing.setImage(new Image("/packaging/batches/time/TimeKing.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/time/TimeKing.png");
        }

        showLegendIfNecessary(withAnimation);
    }
    private void updateImages(int points, boolean withAnimation) {
        if (points >= 25 && !exerciseNovice.getImage().getUrl().contains("/packaging/batches/exercise/ExerciseNovice.png")) {
            exerciseNovice.setImage(new Image("/packaging/batches/exercise/ExerciseNovice.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/exercise/ExerciseNovice.png");
        }
        if (points >= 75 && !exerciseIntermediate.getImage().getUrl().contains("/packaging/batches/exercise/ExerciseIntermediate.png")) {
            exerciseIntermediate.setImage(new Image("/packaging/batches/exercise/ExerciseIntermediate.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/exercise/ExerciseIntermediate.png");
        }
        if (points >= 150 && !exerciseMaster.getImage().getUrl().contains("/packaging/batches/exercise/ExerciseMaster.png")) {
            exerciseMaster.setImage(new Image("/packaging/batches/exercise/ExerciseMaster.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/exercise/ExerciseMaster.png");
        }
        if (points >= 450 && !exerciseKing.getImage().getUrl().contains("/packaging/batches/exercise/ExerciseKing.png")) {
            exerciseKing.setImage(new Image("/packaging/batches/exercise/ExerciseKing.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/exercise/ExerciseKing.png");
        }

        showLegendIfNecessary(withAnimation);
    }

    private void showLegendIfNecessary(boolean withAnimation) {
        if (timeKing.getImage().getUrl().contains("/packaging/batches/time/TimeKing.png") &&
                exerciseKing.getImage().getUrl().contains("/packaging/batches/exercise/ExerciseKing.png") &&
                legend.getImage().getUrl().contains("/packaging/batches/legend/LegendWithoutSaturation.png")) {

            legend.setImage(new Image("/packaging/batches/legend/Legend.png", true));
            if (withAnimation) batchOverlayContainer.show("/packaging/batches/legend/Legend.png");
        }
    }
}
