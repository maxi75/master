package de.hausknecht.master.usecase;

import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.service.automata.registries.NodeDefinitionRegistry;
import de.hausknecht.master.entity.service.automata.registries.NodeRegistry;
import de.hausknecht.master.entity.service.automata.registries.TransitionRegistry;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NodeAdministrator {
    private final NodeRegistry nodeRegistry;
    private final NodeDefinitionRegistry nodeDefinitionRegistry;
    private final TransitionRegistry transitionRegistry;

    public void removeNodeFromNodeRegistry(String nodeName) {
        nodeRegistry.removeNode(nodeName);
        nodeDefinitionRegistry.removeNode(nodeName);
        transitionRegistry.removeAllTransitionsThatContainNode(nodeName);
    }

    public void removeNodeFromTransitionRegistry(TransitionTriple transition) {
        transitionRegistry.removeTransition(transition);
    }

    public boolean addTransition(TransitionTriple transition) {
        return transitionRegistry.addTransition(transition);
    }

    public boolean addNode(String name) {
        return nodeRegistry.addNode(name);
    }

    public ObservableList<String> getNodes() {
        return nodeRegistry.getNodes();
    }

    public void setStartingNode(String nodeName) {
        nodeDefinitionRegistry.setStartingNode(nodeName);
    }

    public boolean addEndingNode(String name) {
        return nodeDefinitionRegistry.addNode(name);
    }

    public void removeEndingNode(String nodeName) {
        nodeDefinitionRegistry.removeNode(nodeName);
    }
}
