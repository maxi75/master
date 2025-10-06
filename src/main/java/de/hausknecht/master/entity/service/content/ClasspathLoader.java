package de.hausknecht.master.entity.service.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClasspathLoader {

    private final ObjectMapper objectMapper;

    public <T> Optional<T> getJsonFromResource(String fileName, Class<T> type) {
        return getClasspathURL(fileName)
                .flatMap(url -> {
                   try { return Optional.of(objectMapper.readValue(url, type)); }
                   catch (IOException e) { return Optional.empty(); }});
    }

    public Optional<String> getStringFromResource(String fileName) {
        return getClasspathURL(fileName).map(String::valueOf);
    }

    private Optional<URL> getClasspathURL(String path) {
        Resource resource = new ClassPathResource(path);
        try {
            return resource.exists() ? Optional.of(resource.getURL()) : Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
