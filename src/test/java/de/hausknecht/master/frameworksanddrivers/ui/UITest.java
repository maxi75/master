package de.hausknecht.master.frameworksanddrivers.ui;

import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UITest {

    @BeforeAll
    static void init() {
        new JFXPanel();
        Graphviz.useEngine(new GraphvizCmdLineEngine());
        fxWait(1000);
    }

    protected static void fxWait(int millis) {
        CountDownLatch latch = new CountDownLatch(1000);
        Platform.runLater(latch::countDown);
        try {
            boolean ignored = latch.await(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {}
    }
}
