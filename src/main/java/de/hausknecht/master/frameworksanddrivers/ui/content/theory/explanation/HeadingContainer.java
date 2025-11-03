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
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeadingContainer {
    static final String CSS_PAGE_THEORY_MAIN_HEADING = "theory-main-heading";
    static final String CSS_PAGE_THEORY_SUB_HEADING = "theory-sub-heading";

    public void addHeadingToTheoryContainer(TheoryPageData theoryPageData, VBox theoryContainer) {
        if (!StringUtils.hasText(theoryPageData.getMainHeading())) return;

        Label mainHeading = new Label(theoryPageData.getMainHeading());
        mainHeading.setId(CSS_PAGE_THEORY_MAIN_HEADING);
        mainHeading.setWrapText(true);
        theoryContainer.getChildren().add(mainHeading);
    }

    public void addSubHeading(TheoryPageData.Section section, VBox theoryContainer) {
        if (!StringUtils.hasText(section.getHeading())) return;

        Label subHeading = new Label(section.getHeading());
        subHeading.getStyleClass().add(CSS_PAGE_THEORY_SUB_HEADING);
        subHeading.setWrapText(true);
        theoryContainer.getChildren().add(subHeading);
    }
}
