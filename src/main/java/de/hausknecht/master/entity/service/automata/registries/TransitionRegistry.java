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

import de.hausknecht.master.entity.domain.automata.TransitionTriple;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.TransitionRemovedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Component
@AllArgsConstructor
public class TransitionRegistry {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<TransitionTriple> transitions = new ArrayList<>();

    public boolean addTransition(TransitionTriple transition) {
        if (transition == null ||
                isBlank(transition.fromNode()) ||
                isBlank(transition.toNode()) ||
                isBlank(transition.transitionWord()) ||
                transition.transitionWord().chars().anyMatch(Character::isWhitespace)) return false;

        boolean alreadyExisting = transitions.stream().anyMatch(transitionTriple ->
                transitionTriple.fromNode().equals(transition.fromNode()) &&
                transitionTriple.toNode().equals(transition.toNode()) &&
                transitionTriple.transitionWord().equals(transition.transitionWord()));

        if (alreadyExisting) return false;

        transitions.add(transition);
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
        return true;
    }

    public void removeAllTransitionsThatContainNode(String containingNode) {
        if (isBlank(containingNode)) return;

        List<TransitionTriple> toRemove = transitions.stream()
                .filter(transitionTriple -> transitionTriple.fromNode().equals(containingNode) ||
                        transitionTriple.toNode().equals(containingNode))
                .toList();

        if (toRemove.isEmpty()) return;
        toRemove.forEach(this::removeTransition);
    }

    public void removeTransition(TransitionTriple transitionTriple) {
        if (transitionTriple == null || isBlank(transitionTriple.fromNode()) || isBlank(transitionTriple.toNode()) || isBlank(transitionTriple.transitionWord())) return;

        TransitionTriple foundTriple = transitions.stream().filter(transition ->
                transitionTriple.fromNode().equals(transition.fromNode()) &&
                transitionTriple.toNode().equals(transition.toNode()) &&
                transitionTriple.transitionWord().equals(transition.transitionWord()))
                .findFirst().orElse(null);

        boolean removed = foundTriple != null && transitions.remove(foundTriple);

        if (removed) {
            applicationEventPublisher.publishEvent(
                    new TransitionRemovedEvent(foundTriple.fromNode(), foundTriple.toNode(), foundTriple.transitionWord()));
            applicationEventPublisher.publishEvent(new GraphChangedEvent());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
