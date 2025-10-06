package de.hausknecht.master.entity.service.automata.registries;

import de.hausknecht.master.entity.domain.automata.AutomataSimulation;
import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class GraphRegistry {
    private final ApplicationEventPublisher applicationEventPublisher;
    private AutomataSimulation selectedGraph = AutomataSimulation.DFA;

    public void changeSelectedGraph(AutomataSimulation selectedGraph) {
        this.selectedGraph = selectedGraph;
        applicationEventPublisher.publishEvent(new GraphChangedEvent());
    }
}
