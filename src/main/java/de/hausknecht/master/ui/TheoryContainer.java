package de.hausknecht.master.ui;

import de.hausknecht.master.entity.TheoryPageData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class TheoryContainer {
    static final String CLASS_PATH_JSON_PREFIX = "theory/json/";
    static final String CLASS_PATH_IMAGE_PREFIX = "theory/images/";
    static final String LOAD_IMAGE_EXCEPTION = "[Warning: An image could not be loaded]";
    static final String LOAD_PAGE_EXCEPTION = "[ERROR: Page not found]";
    static final String FIRST_PAGE = "chapterOneAlphabet.json";

    private final ObjectMapper objectMapper;

    @FXML private VBox theoryContainer;

    @FXML
    public void initialize() {
        theoryContainer.setPadding(new Insets(30, 100, 20, 100));
        renderTheoryData(FIRST_PAGE);
    }

    public void renderTheoryData(String fileName) {
        deleteAlreadyExistingContent();

        Resource jsonFile = new ClassPathResource(CLASS_PATH_JSON_PREFIX + fileName);
        try (InputStream jsonReader = jsonFile.getInputStream()){
            TheoryPageData theoryPageData = objectMapper.readValue(jsonReader, TheoryPageData.class);
            buildTheoryContent(theoryPageData);
        } catch (IOException exception) {
            buildErrorPage();
        }
    }

    private void deleteAlreadyExistingContent()
    {
        theoryContainer.getChildren().removeAll(theoryContainer.getChildren());
    }

    private void buildTheoryContent(TheoryPageData theoryPageData) {
        addHeadingToTheoryContainer(theoryPageData);
        theoryPageData.getContent().forEach(this::addContentSections);
    }

    private void addHeadingToTheoryContainer(TheoryPageData theoryPageData) {
        if (!StringUtils.hasText(theoryPageData.getMainHeading())) return;

        Label mainHeading = new Label(theoryPageData.getMainHeading());
        mainHeading.setId("theory-main-heading");
        mainHeading.setWrapText(true);
        theoryContainer.getChildren().add(mainHeading);
    }

    private void addContentSections(TheoryPageData.Section section) {
        addSubHeading(section);
        addText(section);
        addImages(section);
    }

    private void addSubHeading(TheoryPageData.Section section)
    {
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

        ImageView image = loadImage(section.getImage());
        if (image == null) {
            buildErrorImage();
            return;
        }

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

    private ImageView loadImage(String image) {
        Resource imageFile = new ClassPathResource(CLASS_PATH_IMAGE_PREFIX + image);
        try (InputStream imageReader = imageFile.getInputStream()) {
            return new ImageView(new Image(imageReader));
        } catch (Exception exception) {
            return null;
        }
    }

    private void buildErrorImage() {
        showError("image-exception",  LOAD_IMAGE_EXCEPTION);
    }

    private void buildErrorPage() {
        showError("page-exception",  LOAD_PAGE_EXCEPTION);
    }

    private void showError(String cssID, String message) {
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
