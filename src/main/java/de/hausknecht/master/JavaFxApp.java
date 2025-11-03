package de.hausknecht.master;

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

import de.hausknecht.master.frameworksanddrivers.persistence.SessionTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

import static de.hausknecht.master.ConstantProvider.*;

public class JavaFxApp extends Application {
    private ConfigurableApplicationContext context;
    private SessionTime sessionTime;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(MasterApplication.class).run();
        sessionTime = context.getBean(SessionTime.class);
    }

    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML_PATH));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        ConstantProvider.getCSSFiles().forEach(cssFile ->
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssFile)).toExternalForm()));


        stage.setTitle(APPLICATION_NAME);
        stage.setMaximized(true);
        stage.getIcons().add(
                new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream(MAIN_ICON_PATH))));
        stage.setScene(scene);
        stage.show();

        sessionTime.startSession();
    }

    @Override
    public void stop() {
        if (context != null) context.close();

        sessionTime.endSession();
        Platform.exit();
    }
}
