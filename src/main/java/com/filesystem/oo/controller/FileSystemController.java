package com.filesystem.oo.controller;

import com.filesystem.oo.entities.FileSystemEntity;
import com.filesystem.oo.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filesystem")
public class FileSystemController {

    @Autowired
    private final FileSystemService fileSystemService;

    public FileSystemController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    // Create a new entity
    @PostMapping("/create")
    public ResponseEntity<String> createEntity(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam String parentPath,
            @RequestParam(required = false) String content) {
        try {
            fileSystemService.createEntity(type, name, parentPath, content);
            return ResponseEntity.status(HttpStatus.CREATED).body("Entity created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete an entity
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEntity(@RequestParam String path) {
        try {
            fileSystemService.deleteEntity(path);
            return ResponseEntity.status(HttpStatus.OK).body("Entity deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Move an entity
    @PostMapping("/move")
    public ResponseEntity<String> moveEntity(
            @RequestParam String sourcePath,
            @RequestParam String destinationPath) {
        try {
            fileSystemService.moveEntity(sourcePath, destinationPath);
            return ResponseEntity.status(HttpStatus.OK).body("Entity moved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Write content to a text file
    @PostMapping("/write")
    public ResponseEntity<String> writeToFile(
            @RequestParam String path,
            @RequestParam String content) {
        try {
            fileSystemService.writeToFile(path, content);
            return ResponseEntity.status(HttpStatus.OK).body("Content written to file successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Retrieve details of an entity (optional, for verification)
    @GetMapping("/info")
    public ResponseEntity<?> getEntityInfo(@RequestParam String path) {
        try {
            FileSystemEntity entity = fileSystemService.findEntityByPath(path);
            if (entity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found");
            }
            return ResponseEntity.ok(entity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}