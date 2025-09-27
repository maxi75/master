package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

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

    @Setter private ListElementContainer listElementContainer;

    @FXML
    private void initialize() {
        deleteBtn.setOnAction(_ -> listElementContainer.delete(listElementName.getText(), deleteBtn.getParent()));
    }

    public void setListName(String listName) {
        this.listElementName.setText(listName);
    }
}
