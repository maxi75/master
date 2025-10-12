package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.graphView;

import de.hausknecht.master.entity.domain.eventdata.GraphChangedEvent;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.usecase.GraphAdministrator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
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

    private final GraphAdministrator graphAdministrator;

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
