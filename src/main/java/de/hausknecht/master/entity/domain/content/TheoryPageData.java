package de.hausknecht.master.entity.domain.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TheoryPageData {
    private String mainHeading;
    private List<Section> content;

    @Data
    public static class Section {
        private String heading;
        private String text;
        private String image;
        private String imageAltText;
        private String imageSubText;
        private List<Exercise> exercises;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exercise {
        private ExerciseType kind;
        private String question;
        private List<Integer> correctAnswers;
        private List<String> answers;
        private List<String> nodes;
        private String startingNode;
        private List<String> endingNodes;
        private List<String> transitions;
    }
}
