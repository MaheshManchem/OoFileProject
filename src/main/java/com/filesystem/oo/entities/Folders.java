package com.filesystem.oo.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Folders extends FileSystemEntity{

    private final Map<String, FileSystemEntity> children = new HashMap<>();

    public Folders(String name, String path) {
        super("Folder", name, path);
    }

    public void addEntity(FileSystemEntity entity) {
        children.put(entity.getName(), entity);
    }

    public FileSystemEntity getEntity(String name) {
        return children.get(name);
    }

    @Override
    public void computeSize() {
        setSize(children.values().stream().mapToInt(FileSystemEntity::getSize).sum());
    }

    @Override
    public boolean isContainer() {
        return true;
    }
}
