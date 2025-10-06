package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TextContainer {

    public void addText(TheoryPageData.Section section, VBox theoryContainer)
    {
        if (!StringUtils.hasText(section.getText())) return;

        Label text = new Label(section.getText());
        text.getStyleClass().add("theory-text");
        text.setWrapText(true);
        theoryContainer.getChildren().add(text);
    }
}
