package de.hausknecht.master;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

public class JavaFxApp extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(MasterApplication.class).run();
    }

    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/Main.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ui/css/overlay.css")).toExternalForm());
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ui/css/menu.css")).toExternalForm());
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ui/css/splitPane.css")).toExternalForm());
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ui/css/theory.css")).toExternalForm());
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ui/css/simulation.css")).toExternalForm());

        stage.setTitle("Automata Theory");
        stage.setMaximized(true);
        stage.getIcons().add(new javafx.scene.image.Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/packaging/icon.png"))));

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
        Platform.exit();
    }
}
