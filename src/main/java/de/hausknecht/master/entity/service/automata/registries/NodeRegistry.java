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
