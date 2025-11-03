package de.hausknecht.master.entity.service.content;

/*-
 * #%L
 * master
 * %%
 * Copyright (C) 2025 Maximilian Hausknecht
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
        if (path == null) return Optional.empty();

        Resource resource = new ClassPathResource(path);
        try {
            return resource.exists() ? Optional.of(resource.getURL()) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
