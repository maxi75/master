package de.hausknecht.master.entity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NodeRegistry {
    private final ObservableList<String> nodes = FXCollections.observableArrayList();

    public boolean addNode(String name){
        if (name == null || name.isBlank() || nodes.contains(name)) return false;
        return nodes.add(name);
    }

    public void removeNode(String name){
        nodes.remove(name);
    }
}
