package de.hausknecht.master.usecase;

import de.hausknecht.master.entity.domain.TheoryPageData;
import de.hausknecht.master.entity.service.ClasspathLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClasspathData {
    static final String CLASS_PATH_JSON_PREFIX = "theory/json/";
    static final String CLASS_PATH_IMAGE_PREFIX = "theory/images/";

    private final ClasspathLoader classpathLoader;

    public TheoryPageData getNewPageData(String fileName) {
        return classpathLoader
                .getJsonFromResource(CLASS_PATH_JSON_PREFIX + fileName, TheoryPageData.class)
                .orElse(null);
    }

    public String loadImage(String image) {
        return classpathLoader
                .getStringFromResource(CLASS_PATH_IMAGE_PREFIX + image)
                .orElse(null);
    }
}
