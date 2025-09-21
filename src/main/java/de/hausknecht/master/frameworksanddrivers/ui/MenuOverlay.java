package de.hausknecht.master.frameworksanddrivers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuOverlay {
    private final TheoryContainer theoryContainer;

    @FXML private VBox menuOverlay;
    @FXML private Button closeButton;

    @FXML
    public void initialize() {
        menuOverlay.setMaxWidth(Region.USE_PREF_SIZE);
        menuOverlay.setMaxHeight(Double.MAX_VALUE);
        menuOverlay.setPadding(new Insets(30, 50, 20, 50));
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);

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

    private void closeMenu() {
        menuOverlay.setVisible(false);
    }

    public void openMenu() {
        menuOverlay.setVisible(true);
    }
}
