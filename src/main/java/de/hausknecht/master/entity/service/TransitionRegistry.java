package de.hausknecht.master.entity.service;

import de.hausknecht.master.entity.domain.TransitionTriple;
import de.hausknecht.master.entity.domain.eventdata.GraphChanged;
import de.hausknecht.master.entity.domain.eventdata.TransitionRemoved;
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
        if (isBlank(transition.fromNode()) ||
                isBlank(transition.toNode()) ||
                isBlank(transition.transitionWord()) ||
                transition.transitionWord().chars().anyMatch(Character::isWhitespace)) return false;

        boolean alreadyExisting = transitions.stream().anyMatch(transitionTriple ->
                transitionTriple.fromNode().equals(transition.fromNode()) &&
                transitionTriple.toNode().equals(transition.toNode()) &&
                transitionTriple.transitionWord().equals(transition.transitionWord()));

        if (alreadyExisting) return false;

        applicationEventPublisher.publishEvent(new GraphChanged());
        transitions.add(transition);
        return true;
    }

    public void removeAllNodesThatContainNode(String containingNode) {
        if (isBlank(containingNode)) return;

        List<TransitionTriple> toRemove = transitions.stream()
                .filter(transitionTriple -> transitionTriple.fromNode().equals(containingNode) ||
                        transitionTriple.toNode().equals(containingNode))
                .toList();

        if (toRemove.isEmpty()) return;
        toRemove.forEach(this::removeNode);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public void removeNode(TransitionTriple transitionTriple) {
        if (isBlank(transitionTriple.fromNode()) || isBlank(transitionTriple.toNode()) || isBlank(transitionTriple.transitionWord())) return;

        TransitionTriple foundTriple = transitions.stream().filter(transition ->
                transitionTriple.fromNode().equals(transition.fromNode()) &&
                transitionTriple.toNode().equals(transition.toNode()) &&
                transitionTriple.transitionWord().equals(transition.transitionWord()))
                .findFirst().orElse(null);

        boolean removed = foundTriple != null && transitions.remove(foundTriple);

        if (removed) {
            applicationEventPublisher.publishEvent(
                    new TransitionRemoved(foundTriple.fromNode(), foundTriple.toNode(), foundTriple.transitionWord()));
            applicationEventPublisher.publishEvent(new GraphChanged());
        }
    }
}
