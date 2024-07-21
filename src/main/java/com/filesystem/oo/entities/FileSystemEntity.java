package com.filesystem.oo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public abstract class FileSystemEntity {

    private String type;
    private String name;
    private String path;
    private int size;


    public FileSystemEntity(String type, String name, String path) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public abstract void computeSize();

    public abstract boolean isContainer();

}
