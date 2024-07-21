package com.filesystem.oo.ControllerTest;

import com.filesystem.oo.controller.FileSystemController;
import com.filesystem.oo.entities.FileSystemEntity;
import com.filesystem.oo.entities.TextFile;
import com.filesystem.oo.service.FileSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(FileSystemController.class)
    public class FileSystemControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FileSystemService fileSystemService;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testCreateEntity_Success() throws Exception {
            mockMvc.perform(post("/api/filesystem/create")
                            .param("type", "TextFile")
                            .param("name", "file1.txt")
                            .param("parentPath", "Drive1\\Folder1")
                            .param("content", "Hello World")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Entity created successfully"));

            verify(fileSystemService, times(1)).createEntity("TextFile", "file1.txt", "Drive1\\Folder1", "Hello World");
        }

        @Test
        public void testCreateEntity_Failure() throws Exception {
            doThrow(new RuntimeException("Path already exists")).when(fileSystemService)
                    .createEntity(anyString(), anyString(), anyString(), anyString());

            mockMvc.perform(post("/api/filesystem/create")
                            .param("type", "TextFile")
                            .param("name", "file1.txt")
                            .param("parentPath", "Drive1\\Folder1")
                            .param("content", "Hello World")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Path already exists"));
        }

        @Test
        public void testDeleteEntity_Success() throws Exception {
            mockMvc.perform(delete("/api/filesystem/delete")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Entity deleted successfully"));

            verify(fileSystemService, times(1)).deleteEntity("Drive1\\Folder1\\file1.txt");
        }

        @Test
        public void testDeleteEntity_Failure() throws Exception {
            doThrow(new RuntimeException("Path not found")).when(fileSystemService)
                    .deleteEntity(anyString());

            mockMvc.perform(delete("/api/filesystem/delete")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Path not found"));
        }

        @Test
        public void testMoveEntity_Success() throws Exception {
            mockMvc.perform(post("/api/filesystem/move")
                            .param("sourcePath", "Drive1\\Folder1\\file1.txt")
                            .param("destinationPath", "Drive1\\Folder2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Entity moved successfully"));

            verify(fileSystemService, times(1)).moveEntity("Drive1\\Folder1\\file1.txt", "Drive1\\Folder2");
        }

        @Test
        public void testMoveEntity_Failure() throws Exception {
            doThrow(new RuntimeException("Path not found")).when(fileSystemService)
                    .moveEntity(anyString(), anyString());

            mockMvc.perform(post("/api/filesystem/move")
                            .param("sourcePath", "Drive1\\Folder1\\file1.txt")
                            .param("destinationPath", "Drive1\\Folder2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Path not found"));
        }

        @Test
        public void testWriteToFile_Success() throws Exception {
            mockMvc.perform(post("/api/filesystem/write")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .param("content", "New Content")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Content written to file successfully"));

            verify(fileSystemService, times(1)).writeToFile("Drive1\\Folder1\\file1.txt", "New Content");
        }

        @Test
        public void testWriteToFile_Failure() throws Exception {
            doThrow(new RuntimeException("Path not found or not a text file")).when(fileSystemService)
                    .writeToFile(anyString(), anyString());

            mockMvc.perform(post("/api/filesystem/write")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .param("content", "New Content")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Path not found or not a text file"));
        }

        @Test
        public void testGetEntityInfo_Success() throws Exception {
            FileSystemEntity entity = new TextFile("file1.txt", "Drive1\\Folder1\\file1.txt", "Hello World");
            when(fileSystemService.findEntityByPath("Drive1\\Folder1\\file1.txt")).thenReturn(entity);

            mockMvc.perform(get("/api/filesystem/info")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("file1.txt"))
                    .andExpect(jsonPath("$.content").value("Hello World"));

            verify(fileSystemService, times(1)).findEntityByPath("Drive1\\Folder1\\file1.txt");
        }

        @Test
        public void testGetEntityInfo_Failure() throws Exception {
            when(fileSystemService.findEntityByPath("Drive1\\Folder1\\file1.txt")).thenReturn(null);

            mockMvc.perform(get("/api/filesystem/info")
                            .param("path", "Drive1\\Folder1\\file1.txt")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Entity not found"));
        }
    }

