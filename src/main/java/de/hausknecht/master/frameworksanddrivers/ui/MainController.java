package de.hausknecht.master.frameworksanddrivers.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainController {

    @FXML private Node menuOverlay;

    @FXML
    public void initialize(){
        StackPane.setAlignment(menuOverlay, Pos.TOP_LEFT);
    }
}
