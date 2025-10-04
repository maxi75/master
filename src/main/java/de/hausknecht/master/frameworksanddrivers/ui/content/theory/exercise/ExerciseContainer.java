package de.hausknecht.master.frameworksanddrivers.ui.content.theory.exercise;

import de.hausknecht.master.entity.domain.TheoryPageData;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseContainer {
    private final MultipleChoice multipleChoice;

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
            case MULTIPLE_CHOICE -> multipleChoice.addMultipleChoiceExercise(exercise, container);
            case DEA -> System.out.println("df");
            case NEA -> System.out.println("e3fsa");
        }
    }
}
