package de.hausknecht.master.frameworksanddrivers.ui;

import de.hausknecht.master.entity.domain.TheoryPageData;
import de.hausknecht.master.usecase.ClasspathData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TheoryContainer {
    static final String LOAD_PAGE_EXCEPTION = "[ERROR: Page not found]";
    static final String LOAD_IMAGE_EXCEPTION = "[Warning: An image could not be loaded]";
    static final String FIRST_PAGE = "chapterOneAlphabet.json";

    @FXML private VBox theoryContainer;

    private final ClasspathData classpathData;

    @FXML
    public void initialize() {
        theoryContainer.setPadding(new Insets(30, 100, 20, 100));
        renderTheoryData(FIRST_PAGE);
    }

    public void renderTheoryData(String fileName) {
        deleteAlreadyExistingContent();

        TheoryPageData theoryPageData = classpathData.getNewPageData(fileName);
        if (theoryPageData == null) showError("page-exception", LOAD_PAGE_EXCEPTION);
        if (theoryPageData != null) renderNewPage(theoryPageData);
    }

    private void deleteAlreadyExistingContent() {
        theoryContainer.getChildren().removeAll(theoryContainer.getChildren());
    }

    private void renderNewPage(TheoryPageData theoryPageData) {
        addHeadingToTheoryContainer(theoryPageData);
        theoryPageData.getContent().forEach(this::addContentSections);
    }

    private void addContentSections(TheoryPageData.Section section) {
        addSubHeading(section);
        addText(section);
        addImages(section);
    }

    private void addHeadingToTheoryContainer(TheoryPageData theoryPageData) {
        if (!StringUtils.hasText(theoryPageData.getMainHeading())) return;

        Label mainHeading = new Label(theoryPageData.getMainHeading());
        mainHeading.setId("theory-main-heading");
        mainHeading.setWrapText(true);
        theoryContainer.getChildren().add(mainHeading);
    }

    private void addSubHeading(TheoryPageData.Section section) {
        if (!StringUtils.hasText(section.getHeading())) return;

        Label subHeading = new Label(section.getHeading());
        subHeading.getStyleClass().add("theory-sub-heading");
        subHeading.setWrapText(true);
        theoryContainer.getChildren().add(subHeading);
    }

    private void addText(TheoryPageData.Section section)
    {
        if (!StringUtils.hasText(section.getText())) return;

        Label text = new Label(section.getText());
        text.getStyleClass().add("theory-text");
        text.setWrapText(true);
        theoryContainer.getChildren().add(text);
    }

    private void addImages(TheoryPageData.Section section)
    {
        if (!StringUtils.hasText(section.getImage())) return;

        String imageString = classpathData.loadImage(section.getImage());
        if (imageString == null) {
            showError("image-exception",  LOAD_IMAGE_EXCEPTION);
            return;
        }

        ImageView image = new ImageView(new Image(imageString, true));
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.getStyleClass().add("theory-image");
        if (StringUtils.hasText(section.getImageAltText()))
            image.setAccessibleText(section.getImageAltText());

        VBox imageContainer = new VBox();
        imageContainer.getStyleClass().add("image-container");
        image.fitWidthProperty().bind(imageContainer.widthProperty().multiply(0.60));

        imageContainer.getChildren().add(image);
        addCaption(imageContainer, section);
        theoryContainer.getChildren().add(imageContainer);
    }

    private void addCaption(VBox imageContainer, TheoryPageData.Section section) {
        if (!StringUtils.hasText(section.getImageSubText())) return;

        Label caption = new Label(section.getText());
        caption.getStyleClass().add("image-caption");
        caption.setWrapText(true);
        imageContainer.getChildren().add(caption);
    }

    public void showError(String cssID, String message) {
        System.out.println(message);

        VBox exceptionContainer = new VBox();
        exceptionContainer.getStyleClass().add("exception-container");

        Label exception = new Label(message);
        exception.getStyleClass().add(cssID);
        exception.setWrapText(true);

        exceptionContainer.getChildren().add(exception);
        theoryContainer.getChildren().add(exceptionContainer);
    }
}
