package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.graphView;

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

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentType;
import de.hausknecht.master.usecase.GraphAdministrator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static de.hausknecht.master.ConstantProvider.*;

@Component
@RequiredArgsConstructor
public class GraphView {
    static final String ERROR_WHILE_RENDERING = "Error while trying to render graph: ";
    static final String SVG_MEDIATYPE = "image/svg+xml";

    @FXML WebView graphView;
    @FXML Button fullsize;

    private final GraphAdministrator graphAdministrator;
    private final ApplicationEventPublisher applicationEventPublisher;

    private boolean isFullsize = true;

    @FXML
    public void initialize() {
        configureZoom();
        Optional<String> graphDefinition = graphAdministrator.returnGraphDefinition();
        renderGraph(graphDefinition);
    }

    private void configureZoom() {
        graphView.setZoom(1.0);
        graphView.setOnScroll(event -> {
            if (event.isControlDown()) {
                double zoom = graphView.getZoom() + (event.getDeltaY() > 0 ? GRAPH_ZOOM_STEP : -GRAPH_ZOOM_STEP);
                zoom = Math.max(zoom, GRAPH_ZOOM_MIN);
                zoom = Math.min(zoom, GRAPH_ZOOM_MAX);
                graphView.setZoom(zoom);
            }
        });
    }

    @FXML
    public void toggleTheory() {
        applicationEventPublisher.publishEvent(new ToggleContentEvent(ToggleContentType.SIMULATION));
        if (isFullsize) fullsize.setText("⤡");
        else fullsize.setText("⤢");
        isFullsize = !isFullsize;
    }

    @EventListener
    public void onGraphChanged(GraphChangedEvent ignored) {
        javafx.application.Platform.runLater(() -> {
            Optional<String> graphDefinition = graphAdministrator.returnGraphDefinition();
            this.renderGraph(graphDefinition);
        });
    }

    @EventListener
    public void simulatedGraph(SimulationEvent event) {
        javafx.application.Platform.runLater(() -> {
            Optional<String> graphDefinition = graphAdministrator.returnSimulatedGraphDefinition(event.input());
            this.renderGraph(graphDefinition);
        });
    }

    private void renderGraph(Optional<String> graphDefinition) {
        if (graphDefinition.isEmpty()) return;

        try {
            String svg = Graphviz.fromString(graphDefinition.get())
                    .render(Format.SVG).toString();

            graphView.getEngine().loadContent(svg, SVG_MEDIATYPE);
        } catch (Exception e) {
            System.err.println(ERROR_WHILE_RENDERING + e.getMessage());
        }
    }
}
