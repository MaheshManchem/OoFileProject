package com.filesystem.oo.entities;

import lombok.Getter;

@Getter
public class TextFile extends FileSystemEntity{
    private String content;

    public TextFile(String name, String path, String content) {
        super("TextFile", name, path);
        this.content = content;
        computeSize();
    }

    public void setContent(String content) {
        this.content = content;
        computeSize();
    }

    @Override
    public void computeSize() {
        setSize(content.length());
    }

    @Override
    public boolean isContainer() {
        return false;
    }
}
