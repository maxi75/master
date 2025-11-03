package de.hausknecht.master.usecase;

/*-
 * #%L
 * master
 * %%
 * Copyright (C) 2025 Maximilian Hausknecht
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
