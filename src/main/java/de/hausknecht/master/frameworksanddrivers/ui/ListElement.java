package de.hausknecht.master.frameworksanddrivers.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListElement {
    @FXML private Label listElementName;
    @FXML private Button deleteBtn;

    @Setter private NodeContainer nodeContainer;

    @FXML
    private void initialize() {
        deleteBtn.setOnAction(_ -> nodeContainer.delete(listElementName.getText(), deleteBtn.getParent()));
    }

    public void setListName(String listName) {
        this.listElementName.setText(listName);
    }
}
