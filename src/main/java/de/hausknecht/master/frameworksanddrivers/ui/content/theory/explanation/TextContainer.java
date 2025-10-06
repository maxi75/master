package de.hausknecht.master.frameworksanddrivers.ui.content.theory.explanation;

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TextContainer {
    static final String CSS_PAGE_THEORY_TEXT = "theory-text";

    public void addText(TheoryPageData.Section section, VBox theoryContainer)
    {
        if (!StringUtils.hasText(section.getText())) return;

        Label text = new Label(section.getText());
        text.getStyleClass().add(CSS_PAGE_THEORY_TEXT);
        text.setWrapText(true);
        theoryContainer.getChildren().add(text);
    }
}
