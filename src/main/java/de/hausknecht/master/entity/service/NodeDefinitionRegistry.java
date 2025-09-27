package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemoved;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@RequiredArgsConstructor
public class NodeDefinitionRegistry {
    private String startingNode;
    private final ObservableList<String> endingNodes = FXCollections.observableArrayList();

    private final ApplicationEventPublisher applicationEventPublisher;

    public boolean addNode(String name){
        if (name == null || name.isBlank() || endingNodes.contains(name)) return false;
        return endingNodes.add(name);
    }

    public void removeNode(String name){
        if (endingNodes.remove(name))
            applicationEventPublisher.publishEvent(
                    new EndingNodeRemoved(name));
    }
}
