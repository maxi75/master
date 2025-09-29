package de.hausknecht.master.frameworksanddrivers.ui.content.simulation.graphView;

import de.hausknecht.master.entity.domain.eventdata.GraphChanged;
import de.hausknecht.master.entity.domain.eventdata.SimulationEvent;
import de.hausknecht.master.interfaceadapters.GraphAdministrator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GraphView {
    private static final String SVG_MEDIATYPE = "image/svg+xml";

    @FXML private WebView graphView;

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
                double zoom = graphView.getZoom() + (event.getDeltaY() > 0 ? 0.1 : -0.1);
                zoom = Math.max(zoom, 0.2);
                zoom = Math.min(zoom, 5.0);
                graphView.setZoom(zoom);
            }
        });
    }

    @EventListener
    public void onGraphChanged(GraphChanged event) {
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
            System.err.println("Error while trying to render graph: " + e.getMessage());
        }
    }
}
