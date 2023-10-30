package com.example.picas.models;

public class FileModal {
    public String name, path, type;
    public int size;

    public void setName(String file_name) {
        this.name = file_name;
    }

    public void setPath(String file_path) {
        this.path = file_path;
    }

    public void setType(String file_type) {
        this.type = file_type;
    }

    public void setSize(int file_size_in_bytes) {
        this.size = file_size_in_bytes;
    }
}
