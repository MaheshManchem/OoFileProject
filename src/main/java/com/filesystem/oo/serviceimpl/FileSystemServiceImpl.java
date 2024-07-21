package com.filesystem.oo.serviceimpl;

import com.filesystem.oo.entities.*;
import com.filesystem.oo.service.FileSystemService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FileSystemServiceImpl implements FileSystemService {

        Map<String, Drive> drives = new HashMap<>();


    // Create a new entity
        public void createEntity(String type, String name, String parentPath, String content) throws Exception {
            Drive driveC = new Drive("C");
            Drive driveD = new Drive("D");

            drives.put(driveC.getName(), driveC);
            drives.put(driveD.getName(), driveD);

            FileSystemEntity parent = findEntityByPath(parentPath);
            if (parent == null) {
                throw new Exception("Path not found");
            }

            if (parent instanceof Drive) {
                if (drives.get(((Drive) parent).getPath()).getEntity(name) != null) {
                    throw new Exception("Path already exists");
                }
                FileSystemEntity newEntity = createEntityOfType(type, name, parentPath, content);
                ((Drive) parent).addEntity(newEntity);
            } else if (parent instanceof Folders) {
                if (((Folders) parent).getEntity(name) != null) {
                    throw new Exception("Path already exists");
                }
                FileSystemEntity newEntity = createEntityOfType(type, name, parentPath, content);
                ((Folders) parent).addEntity(newEntity);
            } else {
                throw new Exception("Illegal File System Operation");
            }

            parent.computeSize();
        }

        private FileSystemEntity createEntityOfType(String type, String name, String parentPath, String content) {
            String path = parentPath + "\\" + name;
            switch (type) {
                case "TextFile":
                    return new TextFile(name, path, content);
                case "Folder":
                    return new Folders(name, path);
                case "ZipFile":
                    return new ZipFile(name, path);
                default:
                    throw new IllegalArgumentException("Invalid entity type");
            }
        }

        // Delete an entity
        public void deleteEntity(String path) throws Exception {
            FileSystemEntity entity = findEntityByPath(path);
            if (entity == null) {
                throw new Exception("Path is not found");
            }

            if (entity instanceof Drive) {
                drives.remove(path);
            } else {
                String parentPath = path.substring(0, path.lastIndexOf("\\"));
                FileSystemEntity parent = findEntityByPath(parentPath);
                if (parent instanceof Folders) {
                    ((Folders) parent).getChildren().remove(entity.getName());
                } else if (parent instanceof ZipFile) {
                    ((ZipFile) parent).getChildren().remove(entity.getName());
                }
            }
        }

        // Move an entity
        public void moveEntity(String sourcePath, String destinationPath) throws Exception {
            FileSystemEntity entity = findEntityByPath(sourcePath);
            FileSystemEntity destinationParent = findEntityByPath(destinationPath);
            if (entity == null || destinationParent == null) {
                throw new Exception("Path not found");
            }
            if (!(destinationParent instanceof Folders) && !(destinationParent instanceof ZipFile)) {
                throw new Exception("Illegal File System Operation");
            }

            String newPath = destinationPath + "\\" + entity.getName();
            if (findEntityByPath(newPath) != null) {
                throw new Exception("Path already exists");
            }

            deleteEntity(sourcePath);
            if (destinationParent instanceof Folders) {
                ((Folders) destinationParent).addEntity(entity);
            } else if (destinationParent instanceof ZipFile) {
                ((ZipFile) destinationParent).addEntity(entity);
            }
            entity.computeSize();
            destinationParent.computeSize();
        }

        // Write to a text file
        public void writeToFile(String path, String content) throws Exception {
            FileSystemEntity entity = findEntityByPath(path);
            if (entity == null || !(entity instanceof TextFile)) {
                throw new Exception("Path not found or not a text file");
            }
            ((TextFile) entity).setContent(content);
            entity.computeSize();
            String parentPath = path.substring(0, path.lastIndexOf("\\"));
            FileSystemEntity parent = findEntityByPath(parentPath);
            parent.computeSize();
        }

        public FileSystemEntity findEntityByPath(String path) {
            String[] pathSegments = path.split("\\\\");
            if (pathSegments.length == 0) {
                return null;
            }

            String driveName = pathSegments[0];
            Drive drive = drives.get(driveName);
            if (drive == null) {
                return null;
            }

            FileSystemEntity current = drive;
            for (int i = 1; i < pathSegments.length; i++) {
                if (current instanceof Folders) {
                    current = ((Folders) current).getEntity(pathSegments[i]);
                } else if (current instanceof ZipFile) {
                    current = ((ZipFile) current).getEntity(pathSegments[i]);
                } else {
                    return null;
                }

                if (current == null) {
                    return null;
                }
            }
            return current;
        }
}
