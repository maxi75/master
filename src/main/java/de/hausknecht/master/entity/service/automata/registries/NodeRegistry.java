package de.hausknecht.master.entity.service.automata.registries;

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
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

        applicationEventPublisher.publishEvent(new GraphChangedEvent());
        return nodes.add(name);
    }

    public void removeNode(String name){
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
        nodes.remove(name);
    }
}
