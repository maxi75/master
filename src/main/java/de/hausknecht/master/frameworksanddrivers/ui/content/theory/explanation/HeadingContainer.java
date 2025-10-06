package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeadingContainer {

    public void addHeadingToTheoryContainer(TheoryPageData theoryPageData, VBox theoryContainer) {
        if (!StringUtils.hasText(theoryPageData.getMainHeading())) return;

        Label mainHeading = new Label(theoryPageData.getMainHeading());
        mainHeading.setId("theory-main-heading");
        mainHeading.setWrapText(true);
        theoryContainer.getChildren().add(mainHeading);
    }

    public void addSubHeading(TheoryPageData.Section section, VBox theoryContainer) {
        if (!StringUtils.hasText(section.getHeading())) return;

        Label subHeading = new Label(section.getHeading());
        subHeading.getStyleClass().add("theory-sub-heading");
        subHeading.setWrapText(true);
        theoryContainer.getChildren().add(subHeading);
    }
}
