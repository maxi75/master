package de.hausknecht.master.frameworksanddrivers.ui.menu;

import de.hausknecht.master.entity.domain.eventdata.PointsChanged;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.TheoryContainer;
import de.hausknecht.master.interfaceadapters.PointSystemAdministrator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuOverlay {
    private static final String POINTS = "Punktzahl: ";

    private final TheoryContainer theoryContainer;
    private final PointSystemAdministrator pointSystemAdministrator;

    @FXML private VBox menuOverlay;
    @FXML private Button closeButton;
    @FXML private Label statusPoints;

    @FXML
    public void initialize() {
        menuOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        menuOverlay.setMaxHeight(Double.MAX_VALUE);
        menuOverlay.setPadding(new Insets(30, 50, 20, 50));
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

        statusPoints.setText(POINTS + pointSystemAdministrator.getPoints());

        closeButton.setOnAction(_ -> this.closeMenu());
        this.closeMenu();
    }

    @FXML
    private void loadContent(ActionEvent e) {
        Button menuNavigationButton = (Button) e.getSource();
        String file = (String) menuNavigationButton.getUserData();
        if (file != null && !file.isBlank()) {
            theoryContainer.renderTheoryData(file);
            closeMenu();
        }
    }

    @EventListener
    public void onPointsChanged(PointsChanged ignored) {
        javafx.application.Platform.runLater(() ->
                statusPoints.setText(POINTS + pointSystemAdministrator.getPoints()));
    }

    private void closeMenu() {
        menuOverlay.setVisible(false);
    }

    public void openMenu() {
        menuOverlay.setVisible(true);
    }
}
