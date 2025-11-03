package de.hausknecht.master.entity.service.automata.registries;

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

import de.hausknecht.master.entity.domain.eventdata.EndingNodeRemovedEvent;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
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

        endingNodes.add(name);
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
        return true;
    }

    public void removeNode(String name){
        if (endingNodes.remove(name)) {
            applicationEventPublisher.publishEvent(new EndingNodeRemovedEvent(name));
            applicationEventPublisher.publishEvent(new GraphChangedEvent());
        }
    }

    public void setStartingNode(String name){
        if (name == null || name.isBlank()) return;

        startingNode = name;
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
    }
}
