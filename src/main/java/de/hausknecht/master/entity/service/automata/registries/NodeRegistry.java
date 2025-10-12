package de.hausknecht.master.entity.service.automata.registries;

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Getter
@Component
@AllArgsConstructor
public class NodeRegistry {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObservableList<String> nodes = FXCollections.observableArrayList();

    public boolean addNode(String name){
        if (name == null || name.isBlank() || nodes.contains(name)) return false;

        nodes.add(name);
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
        return true;
    }

    public void removeNode(String name){
        if (name == null || name.isBlank()) return;

        nodes.remove(name);
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
    }
}
