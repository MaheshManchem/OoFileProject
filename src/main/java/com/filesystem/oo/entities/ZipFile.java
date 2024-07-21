package com.filesystem.oo.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ZipFile extends FileSystemEntity{
    private final Map<String, FileSystemEntity> children = new HashMap<>();

    public ZipFile(String name, String path) {
        super("ZipFile", name, path);
    }

    public void addEntity(FileSystemEntity entity) {
        children.put(entity.getName(), entity);
    }

    public FileSystemEntity getEntity(String name) {
        return children.get(name);
    }

    @Override
    public void computeSize() {
        int totalSize = children.values().stream().mapToInt(FileSystemEntity::getSize).sum();
        setSize(totalSize / 2);
    }

    @Override
    public boolean isContainer() {
        return true;
    }
}
