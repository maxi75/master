package de.hausknecht.master.entity.domain;

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
    }
}
