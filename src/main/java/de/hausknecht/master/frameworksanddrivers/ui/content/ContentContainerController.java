package de.hausknecht.master.frameworksanddrivers.ui.content;

import de.hausknecht.master.entity.domain.eventdata.ToggleTheoryEvent;
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

    @FXML private SplitPane contentSplitter;
    @FXML private Node theoryContainer;
    @FXML private Node simulatorContainer;

    @Getter
    private boolean theoryVisible = true;
    private double dividerPosition = 0.5;

    @FXML private void initialize() {
        SplitPane.setResizableWithParent(theoryContainer, true);
        SplitPane.setResizableWithParent(simulatorContainer, true);
    }

    @EventListener
    public void onToggleTheory(ToggleTheoryEvent ignored) {
        javafx.application.Platform.runLater(() -> {
            if (theoryVisible) removeTheoryPane();
            else addTheoryPane();
        });
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
}
