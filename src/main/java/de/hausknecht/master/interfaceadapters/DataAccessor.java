package de.hausknecht.master.interfaceadapters;

import de.hausknecht.master.entity.domain.GraphData;
import de.hausknecht.master.entity.domain.TheoryPageData;
import de.hausknecht.master.entity.service.GraphDataMapper;
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
