package com.example.picas.models;

import androidx.annotation.NonNull;

import com.example.picas.R;

import java.util.Date;

public class FileModal {
    public String name, path;
    public FileTypes type;
//    public FileExtensions extensions;
    public int sizeInBytes;
    public Date lastModifiedDate;

//    public void setName(String file_name) {
//        this.name = file_name;
//    }
    public void setPath(String file_path) {
        this.path = file_path;
    }

//    public void setType(FileTypes file_type) {
//        this.type = file_type;
//    }
//    public void setLastModifiedDate(Date){}

//    public void setSizeInBytes(int file_size_in_bytes) {
//        this.sizeInBytes = file_size_in_bytes;
//    }

    public double getSize(@NonNull FileSizes file_size) {
        switch (file_size) {
            case BYTES:{
                return sizeInBytes;
            }
            case KB: {
                return sizeInBytes / R.integer.bytes_in_kilobyte;
            }
            case MB: {
                return sizeInBytes / R.integer.bytes_in_megabyte;
            }
            case GB: {
                return sizeInBytes / R.integer.bytes_in_gigabyte;
            }
            default: {
                return sizeInBytes + 0.0;
            }
        }
    }

    public String getFormattedSize() {
        int unit = R.integer.bytes_in_kilobyte;

        if (sizeInBytes < unit) {
            return sizeInBytes + " bytes";
        }
        if (sizeInBytes < unit * unit) {
            return getSize(FileSizes.KB) + " kb";
        }
        if (sizeInBytes < unit * unit * unit) {
            return getSize(FileSizes.MB) + " mb";
        }
        return getSize(FileSizes.GB) + " gb";
    }
}

enum FileSizes {
    BYTES, KB, MB, GB
}

enum FileTypes {
    image,gif,video
}
//enum FileExtensions{
//    jpg,jpeg,png,gif,mp4,webp
//}