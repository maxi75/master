package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

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

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ImageContainer {
    static final String CSS_PAGE_IMAGE_EXCEPTION = "image-exception";
    static final String CSS_PAGE_THEORY_IMAGE = "theory-image";
    static final String CSS_PAGE_IMAGE_CAPTION = "image-caption";
    static final String CSS_PAGE_IMAGE_CONTAINER = "image-container";
    static final String LOAD_IMAGE_EXCEPTION = "[Warning: An image could not be loaded]";

    private final ClasspathData classpathData;
    private final ErrorHandler errorHandler;

    public void addImages(TheoryPageData.Section section, VBox theoryContainer)
    {
        if (!StringUtils.hasText(section.getImage())) return;

        String imageString = classpathData.loadImage(section.getImage());
        if (imageString == null) {
            errorHandler.showError(CSS_PAGE_IMAGE_EXCEPTION,  LOAD_IMAGE_EXCEPTION, theoryContainer);
            return;
        }

        ImageView image = new ImageView(new Image(imageString, true));
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.getStyleClass().add(CSS_PAGE_THEORY_IMAGE);
        if (StringUtils.hasText(section.getImageAltText()))
            image.setAccessibleText(section.getImageAltText());

        VBox imageContainer = new VBox();
        imageContainer.getStyleClass().add(CSS_PAGE_IMAGE_CONTAINER);
        image.fitWidthProperty().bind(imageContainer.widthProperty().multiply(0.60));

        imageContainer.getChildren().add(image);
        addCaption(imageContainer, section);
        theoryContainer.getChildren().add(imageContainer);
    }

    private void addCaption(VBox imageContainer, TheoryPageData.Section section) {
        if (!StringUtils.hasText(section.getImageSubText())) return;

        Label caption = new Label(section.getImageSubText());
        caption.getStyleClass().add(CSS_PAGE_IMAGE_CAPTION);
        caption.setWrapText(true);
        imageContainer.getChildren().add(caption);
    }
}
