package de.hausknecht.master.entity.domain;

import net.automatalib.automaton.fsa.impl.CompactNFA;

import java.util.Map;

public record NfaValues(CompactNFA<String> nfa, Map<Integer, String> idToNode, Map<String, Integer> nodeToId) {}
