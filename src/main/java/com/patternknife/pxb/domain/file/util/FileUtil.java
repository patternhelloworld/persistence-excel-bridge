package com.patternknife.pxb.domain.file.util;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return getFileExtension(fileName);
    }

    public static @Nullable String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

}