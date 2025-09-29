package de.hausknecht.master.entity.domain;

import java.util.Set;

public record GraphEvaluationResult(Set<Integer> nodeID, boolean accepted) {}
