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
import javafx.application.Platform;
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
    static final String FIRST_PAGE = "chapterOne/introduction.json";

    @FXML VBox theoryContainer;
    @FXML Button fullsize;
    @FXML Button backButton;
    @FXML Button nextButton;

    private final ClasspathData classpathData;
    private final ErrorHandler errorHandler;
    private final ImageContainer imageContainer;
    private final HeadingContainer headingContainer;
    private final TextContainer textContainer;

    private final ExerciseContainer exerciseContainer;
    private final ApplicationEventPublisher applicationEventPublisher;

    private boolean isFullsize = true;
    String currentFile = FIRST_PAGE;

    @FXML
    void initialize() {
        theoryContainer.setPadding(new Insets(30, 100, 20, 100));
        Platform.runLater(() -> renderTheoryData(currentFile));
    }

    @FXML
    public void toggleTheory() {
        applicationEventPublisher.publishEvent(new ToggleContentEvent(ToggleContentType.THEORY));
        fullsize.setText(isFullsize ? "⤢": "⤡");
        isFullsize = !isFullsize;
    }

    @FXML
    public void back() {
        int index = TheoryPages.PAGES.indexOf(currentFile);
        if (index <= 0) return;
        renderTheoryData(TheoryPages.PAGES.get(index - 1));
    }

    @FXML
    public void next() {
        int index = TheoryPages.PAGES.indexOf(currentFile);
        if (index >= TheoryPages.PAGES.size() - 1) return;
        renderTheoryData(TheoryPages.PAGES.get(index + 1));
    }

    public void renderTheoryData(String fileName) {
        deleteAlreadyExistingContent();

        TheoryPageData theoryPageData = classpathData.getNewPageData(fileName);
        if (theoryPageData == null) errorHandler.showError(CSS_PAGE_EXCEPTION, LOAD_PAGE_EXCEPTION, theoryContainer);
        if (theoryPageData != null) renderNewPage(theoryPageData, fileName);

        updateNavigationButtons();
    }

    private void deleteAlreadyExistingContent() {
        theoryContainer.getChildren().removeAll(theoryContainer.getChildren());
    }

    private void renderNewPage(TheoryPageData theoryPageData, String fileName) {
        currentFile = fileName;
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

    private void updateNavigationButtons() {
        int index = TheoryPages.PAGES.indexOf(currentFile);
        boolean isFirstPage = index == 0;
        boolean isLastPage = index == TheoryPages.PAGES.size() - 1;
        boolean isPageUnknown = index == -1;

        backButton.setVisible(!isFirstPage && !isPageUnknown);
        backButton.setManaged(!isFirstPage && !isPageUnknown);

        nextButton.setVisible(!isLastPage && !isPageUnknown);
        nextButton.setManaged(!isLastPage && !isPageUnknown);
    }
}
