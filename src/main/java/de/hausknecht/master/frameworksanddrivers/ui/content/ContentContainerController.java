package de.hausknecht.master.frameworksanddrivers.ui.content;

import de.hausknecht.master.entity.domain.eventdata.ToggleContentEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class ContentContainerController {

    @FXML SplitPane contentSplitter;
    @FXML Node theoryContainer;
    @FXML Node simulatorContainer;

    boolean theoryVisible = true;
    boolean simulationVisible = true;
    double dividerPosition = 0.5;

    @FXML private void initialize() {
        SplitPane.setResizableWithParent(theoryContainer, true);
        SplitPane.setResizableWithParent(simulatorContainer, true);
        removeSimulationPane();
    }

    @EventListener
    public void onToggleTheory(ToggleContentEvent event) {
        javafx.application.Platform.runLater(() -> handleToggle(event));
    }

    void handleToggle(ToggleContentEvent event) {
        if (event == null) return;

        if (event.type().equals(ToggleContentType.SIMULATION)) {
            if (theoryVisible) removeTheoryPane();
            else addTheoryPane();
        }

        if (event.type().equals(ToggleContentType.THEORY)) {
            if (simulationVisible) removeSimulationPane();
            else addSimulationPane();
        }
    }

    private void removeTheoryPane() {
        double[] positions = contentSplitter.getDividerPositions();
        dividerPosition = positions.length > 0 ? positions[0] : dividerPosition;
        contentSplitter.getItems().remove(theoryContainer);
        theoryVisible = false;
    }

    private void addTheoryPane() {
        ObservableList<Node> items = contentSplitter.getItems();
        if (!items.contains(theoryContainer)) items.addFirst(theoryContainer);

        contentSplitter.setDividerPositions(dividerPosition);
        theoryVisible = true;
    }

    private void removeSimulationPane() {
        double[] positions = contentSplitter.getDividerPositions();
        dividerPosition = positions.length > 0 ? positions[0] : dividerPosition;
        contentSplitter.getItems().remove(simulatorContainer);
        simulationVisible = false;
    }

    private void addSimulationPane() {
        ObservableList<Node> items = contentSplitter.getItems();
        if (!items.contains(simulatorContainer)) items.addLast(simulatorContainer);

        contentSplitter.setDividerPositions(dividerPosition);
        simulationVisible = true;
    }
}
