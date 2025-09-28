package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.eventdata.GraphChanged;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class NodeRegistry {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObservableList<String> nodes = FXCollections.observableArrayList();

    public boolean addNode(String name){
        if (name == null || name.isBlank() || nodes.contains(name)) return false;

        applicationEventPublisher.publishEvent(new GraphChanged());
        return nodes.add(name);
    }

    public void removeNode(String name){
        applicationEventPublisher.publishEvent(new GraphChanged());
        nodes.remove(name);
    }
}
