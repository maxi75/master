package de.hausknecht.master.frameworksanddrivers.ui.content.theory;

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
import de.hausknecht.master.entity.domain.eventdata.ToggleContentEvent;
import de.hausknecht.master.entity.domain.eventdata.ToggleContentType;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise.ExerciseContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ErrorHandler;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.HeadingContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.ImageContainer;
import de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation.TextContainer;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TheoryContainer {
    static final String CSS_PAGE_EXCEPTION = "page-exception";
    static final String LOAD_PAGE_EXCEPTION = "[ERROR: Page not found]";
    static final String FIRST_PAGE = "chapterOne/alphabet.json";

    @FXML VBox theoryContainer;
    @FXML Button fullsize;

    private final ClasspathData classpathData;
    private final ErrorHandler errorHandler;
    private final ImageContainer imageContainer;
    private final HeadingContainer headingContainer;
    private final TextContainer textContainer;

    private final ExerciseContainer exerciseContainer;
    private final ApplicationEventPublisher applicationEventPublisher;

    private boolean isFullsize = true;

    @FXML
    void initialize() {
        theoryContainer.setPadding(new Insets(30, 100, 20, 100));
        renderTheoryData(FIRST_PAGE);
    }

    @FXML
    public void toggleTheory() {
        applicationEventPublisher.publishEvent(new ToggleContentEvent(ToggleContentType.THEORY));
        if (isFullsize) fullsize.setText("⤢");
        else fullsize.setText("⤡");
        isFullsize = !isFullsize;
    }

    public void renderTheoryData(String fileName) {
        deleteAlreadyExistingContent();

        TheoryPageData theoryPageData = classpathData.getNewPageData(fileName);
        if (theoryPageData == null) errorHandler.showError(CSS_PAGE_EXCEPTION, LOAD_PAGE_EXCEPTION, theoryContainer);
        if (theoryPageData != null) renderNewPage(theoryPageData);
    }

    private void deleteAlreadyExistingContent() {
        theoryContainer.getChildren().removeAll(theoryContainer.getChildren());
    }

    private void renderNewPage(TheoryPageData theoryPageData) {
        headingContainer.addHeadingToTheoryContainer(theoryPageData, theoryContainer);
        theoryPageData.getContent().forEach(this::addContentSections);
    }

    private void addContentSections(TheoryPageData.Section section) {
        headingContainer.addSubHeading(section, theoryContainer);
        textContainer.addText(section,  theoryContainer);
        imageContainer.addImages(section, theoryContainer);

        if (section.getExercises() == null) return;
        section.getExercises().forEach(exercise -> exerciseContainer.addExercise(exercise, theoryContainer));
    }
}
