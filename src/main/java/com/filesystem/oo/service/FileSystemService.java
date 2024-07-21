package com.filesystem.oo.service;

import com.filesystem.oo.entities.FileSystemEntity;
import org.springframework.stereotype.Service;


public interface FileSystemService {

    public void createEntity(String type, String name, String parentPath, String content) throws Exception;

    public void deleteEntity(String path) throws Exception;

    public void moveEntity(String sourcePath, String destinationPath) throws Exception;

    public void writeToFile(String path, String content) throws Exception;

    public FileSystemEntity findEntityByPath(String path);

}
