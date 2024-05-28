package com.patternknife.pxb.domain.file.bo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileSystemStorage {

    String saveFile(MultipartFile file) throws IOException;

    Resource loadFile(String fileName) throws FileNotFoundException;
}
