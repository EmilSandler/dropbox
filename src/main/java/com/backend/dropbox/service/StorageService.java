package com.backend.dropbox.service;

import com.backend.dropbox.customExceptions.InvalidDirectoryException;
import com.backend.dropbox.entity.UserFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();

    void store(MultipartFile file, String folderPath) throws InvalidDirectoryException, FileAlreadyExistsException;

    List<UserFile> loadAll(String folderPath);

    Path load(String filename);

    File loadFile(String filepath, String fileName);

    void deleteAll();
}
