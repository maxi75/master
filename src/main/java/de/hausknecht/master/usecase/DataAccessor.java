package de.hausknecht.master.usecase;

import de.hausknecht.master.entity.domain.automata.GraphData;
import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.entity.service.automata.graph.GraphDataMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataAccessor {
    private final GraphDataMapper graphDataMapper;

    public GraphData getGraphDataFromTheoryPageDataExercise(TheoryPageData.Exercise exercise) {
        return graphDataMapper.mapTheoryPageDataExerciseToGraphData(exercise);
    }
}
