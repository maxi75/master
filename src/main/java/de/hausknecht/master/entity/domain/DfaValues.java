package de.hausknecht.master.entity.domain;

import net.automatalib.automaton.fsa.impl.CompactDFA;

import java.util.Map;

public record DfaValues(CompactDFA<String> dfa, Map<Integer, String> idToNode, Map<String, Integer> nodeToId) {}
