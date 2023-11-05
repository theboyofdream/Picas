package com.example.picas.models;

import java.io.File;

public class FolderModal {
    public String name,path;
    public Integer files_count;
    public Long last_modified;

    public FolderModal(String folder_path){
        File folder = new File(folder_path);
        this.name = folder.getName();
        this.path = folder.getAbsolutePath();
        this.last_modified = folder.lastModified();
        this.files_count = folder.list().length;
    }
}
