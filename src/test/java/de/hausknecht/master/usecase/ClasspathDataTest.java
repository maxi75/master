package de.hausknecht.master.usecase;

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

import de.hausknecht.master.TestDataGenerator;
import de.hausknecht.master.entity.service.content.ClasspathLoader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClasspathDataTest {

    private final ClasspathLoader classpathLoaderMock = mock();
    private final ClasspathData classpathData = new ClasspathData(classpathLoaderMock);

    @Nested
    class GetNewPageData {

        @Test
        void getNewPageDataWithValidFile() {
            when(classpathLoaderMock.getJsonFromResource(anyString(), any()))
                    .thenReturn(Optional.of(TestDataGenerator.getCorrectTheoryPageData()));
            assertNotNull(classpathData.getNewPageData("json.json"));
        }

        @Test
        void getNewPageDataWithInvalidFile() {
            when(classpathLoaderMock.getJsonFromResource(anyString(), any()))
                    .thenReturn(Optional.empty());
            assertNull(classpathData.getNewPageData("json.json"));
        }
    }

    @Nested
    class LoadImage {

        @Test
        void getNewPageDataWithValidFile() {
            when(classpathLoaderMock.getStringFromResource(anyString()))
                    .thenReturn(Optional.of(""));
            assertNotNull(classpathData.loadImage("image.svg"));
        }

        @Test
        void getNewPageDataWithInvalidFile() {
            when(classpathLoaderMock.getStringFromResource(anyString()))
                    .thenReturn(Optional.empty());
            assertNull(classpathData.loadImage("image.svg"));
        }
    }
}
