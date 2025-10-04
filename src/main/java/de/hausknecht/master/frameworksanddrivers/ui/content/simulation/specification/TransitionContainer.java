package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.specification;

import de.hausknecht.master.entity.domain.TransitionTriple;
import de.hausknecht.master.entity.domain.eventdata.TransitionRemoved;
import de.hausknecht.master.interfaceadapters.NodeAdministrator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TransitionContainer implements ListElementContainer {
    private static final Pattern NODE_NAME_PATTERN = Pattern.compile("^\\s*(.*?)\\s--\\s*(.*?)\\s*-->\\s*(.*?)\\s*$");

    @FXML private VBox listElementContainer;
    @FXML private Button addButton;
    @FXML private ComboBox<String> fromTransition;
    @FXML private ComboBox<String> toTransition;
    @FXML private TextField transitionWord;

    private final NodeAdministrator nodeAdministrator;

    @FXML
    public void initialize(){
        fromTransition.setItems(nodeAdministrator.getNodes());
        toTransition.setItems(nodeAdministrator.getNodes());
        addButton.setOnAction(_ -> addListItem(
                new TransitionTriple(fromTransition.getValue(), toTransition.getValue(), transitionWord.getText())));
    }

    public void addListItem(TransitionTriple transition) {
        if (!nodeAdministrator.addTransition(transition)) return;
        addListItemToUI(listElementContainer, transition.fromNode() + " --" + transition.transitionWord() + "--> " + transition.toNode());
    }

    public void delete(String nodeName, Parent listItem) {
        if (nodeName == null || nodeName.isBlank() || listItem == null) return;

        Matcher matcher = patternMatcherForExtractingNodeInformationFromText(nodeName);
        if (matcher == null) return;

        nodeAdministrator.removeNodeFromTransitionRegistry(new TransitionTriple(matcher.group(1), matcher.group(3), matcher.group(2)));
        listElementContainer.getChildren().remove(listItem);
    }

    private Matcher patternMatcherForExtractingNodeInformationFromText(String nodeName) {
        Matcher matcher = NODE_NAME_PATTERN.matcher(nodeName);
        if (!matcher.matches()) {
            System.err.println("Matching not possible");
            return null;
        }

        return matcher;
    }

    @EventListener
    public void onNodeRemoved(TransitionRemoved event) {
        javafx.application.Platform.runLater(() ->{
            List<HBox> list = listElementContainer.getChildren().stream()
                    .filter(HBox.class::isInstance)
                    .map(HBox.class::cast)
                    .filter(node -> node.getChildren().stream()
                            .anyMatch(label -> label instanceof Label lbl &&
                                    Objects.equals(lbl.getText(),
                                            event.fromNode() + " --" + event.transitionWord() + "--> " + event.toNode())))
                    .toList();

            listElementContainer.getChildren().removeAll(list);
        });
    }
}
