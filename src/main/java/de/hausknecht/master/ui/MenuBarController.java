package de.hausknecht.master.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuBarController {
    @FXML private VBox menuBar;
    @FXML private Button openMenuButton;

    private final MenuOverlay menuOverlay;

    @FXML
    public void initialize(){
        menuBar.setMaxWidth(Region.USE_PREF_SIZE);
        menuBar.setMaxHeight(Double.MAX_VALUE);
        menuBar.setPadding(new Insets(30, 20, 20, 20));

        openMenuButton.setOnAction(_ -> menuOverlay.openMenu());
    }
}
