package de.hausknecht.master.entity.domain.eventdata;

public record TransitionRemovedEvent(String fromNode, String toNode, String transitionWord) {}
