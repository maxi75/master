package de.hausknecht.master.entity.domain.automata;

import java.util.Set;

public record GraphEvaluationResult(Set<Integer> nodeID, boolean accepted) {}
