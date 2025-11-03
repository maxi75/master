package de.hausknecht.master.usecase;

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
