package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.TheoryPageData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseContainer {
    private final MultipleChoice multipleChoice;
    private final Word word;
    private final ChangeAutomata changeAutomata;

    public void addExercise(TheoryPageData.Exercise exercise, VBox theoryContainer) {
        if (exercise == null || exercise.getKind() == null || theoryContainer == null) return;

        buildContainer(exercise, theoryContainer);
    }

    private void buildContainer(TheoryPageData.Exercise exercise, VBox theoryContainer) {
        VBox container = new VBox();
        container.getChildren().add(buildExerciseBox(exercise));
        theoryContainer.getChildren().add(container);
    }

    private VBox buildExerciseBox(TheoryPageData.Exercise exercise) {
        VBox exerciseBox = new VBox();
        exerciseBox.getStyleClass().add("exerciseBox");
        addExerciseToContainer(exercise,  exerciseBox);
        return exerciseBox;
    }

    private void addExerciseToContainer(TheoryPageData.Exercise exercise, VBox container) {
        switch (exercise.getKind()) {
            case MULTIPLE_CHOICE -> multipleChoice.addMultipleChoiceExercise(exercise, container, this);
            case DEA_WORD, NEA_WORD -> word.addWordExercise(exercise, container, this);
            case DEA_NEA, NEA_DEA -> changeAutomata.addWordExercise(exercise, container, this);
        }
    }

    public void addTitle(VBox container, String name) {
        Label title = new Label(name);
        title.getStyleClass().add("exerciseTitle");
        title.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().add(title);
    }

    public void addQuestion(VBox container, String question) {
        Text questionText = new Text("Frage: " + question);
        questionText.getStyleClass().add("exerciseQuestion");

        TextFlow textFlow = createTextFlow(questionText, container);
        textFlow.getStyleClass().add("exerciseQuestionContainer");
        container.getChildren().add(textFlow);
    }

    public TextFlow createTextFlow(Text text, VBox container) {
        TextFlow  textFlow = new TextFlow(text);
        textFlow.setMaxWidth(Double.MAX_VALUE);
        textFlow.prefWidthProperty().bind(container.widthProperty());
        return textFlow;
    }
}
