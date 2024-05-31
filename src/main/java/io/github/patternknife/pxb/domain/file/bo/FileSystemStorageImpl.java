package io.github.patternknife.pxb.domain.file.bo;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/*
*
* */
public class FileSystemStorageImpl implements FileSystemStorage {

    protected Path absoluteDirectoryPath;

    public FileSystemStorageImpl(String relativeDirectoryPathStr) throws IOException {
        this.absoluteDirectoryPath = Paths.get(relativeDirectoryPathStr)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(this.absoluteDirectoryPath);
    }


    public void saveFile(@NotNull MultipartFile file, @NotNull String fileNameWithoutExt) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileName = fileNameWithoutExt + "." + originalFilename.substring(originalFilename.lastIndexOf(".")+1);

        Path fullFilePath = this.absoluteDirectoryPath.resolve(fileName);
        Files.copy(file.getInputStream(), fullFilePath, StandardCopyOption.REPLACE_EXISTING);

    }

    public Resource loadFile(@NotNull String fileName) throws FileNotFoundException {

        Path file = this.absoluteDirectoryPath.resolve(fileName).normalize();

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Couldn't find the file. (" + fileName  + ")");
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new FileNotFoundException("Couldn't download the file. (" + fileName  + "). (More : " + e.getMessage() + ")");
        }

    }

    public long getFileSize(@NotNull String fileName) throws IOException {
        Path file = this.absoluteDirectoryPath.resolve(fileName).normalize();
        if (Files.exists(file) && Files.isReadable(file)) {
            return Files.size(file);
        } else {
            throw new IOException("Couldn't read the file :: " + fileName);
        }
    }
}
