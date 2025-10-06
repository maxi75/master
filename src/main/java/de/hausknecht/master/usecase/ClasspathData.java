package de.hausknecht.master.usecase;

import de.hausknecht.master.entity.domain.content.TheoryPageData;
import de.hausknecht.master.entity.service.content.ClasspathLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static de.hausknecht.master.ConstantProvider.THEORY_IMAGES_PATH;
import static de.hausknecht.master.ConstantProvider.THEORY_JSON_PATH;

@Service
@RequiredArgsConstructor
public class ClasspathData {
    private final ClasspathLoader classpathLoader;

    public TheoryPageData getNewPageData(String fileName) {
        return classpathLoader
                .getJsonFromResource(THEORY_JSON_PATH + fileName, TheoryPageData.class)
                .orElse(null);
    }

    public String loadImage(String image) {
        return classpathLoader
                .getStringFromResource(THEORY_IMAGES_PATH + image)
                .orElse(null);
    }
}
