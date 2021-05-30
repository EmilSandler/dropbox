package com.backend.dropbox.service;

import com.backend.dropbox.customExceptions.InvalidDirectoryException;
import com.backend.dropbox.entity.UserFile;
import com.backend.dropbox.repository.UserFileRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFileService implements StorageService {

    private final static String ROOT_PATH = "user-uploads-dir";
    private final static boolean IS_FILE = true;
    private final UserFileRepository userFileRepository;

    @Autowired
    public UserFileService(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Override
    public void init() {

    }

    @Override
    public void store(MultipartFile file, String folderPath) throws InvalidDirectoryException, FileAlreadyExistsException {
        String owner = getOwner();
        String originalFilename = file.getOriginalFilename();
        Path path = Paths.get(ROOT_PATH, owner, folderPath);

        if (!Files.exists(path)) {
            throw new InvalidDirectoryException("Invalid Directory");
        }

        path = Paths.get(path.toString(), originalFilename);

        if (Files.exists(path)) {
            throw new FileAlreadyExistsException(String.format("File %s exists!", path));
        }

        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new InvalidDirectoryException("Invalid Directory");
        }

        saveFileInfo(originalFilename, path.toString().replace(originalFilename, ""), owner, IS_FILE);
    }

    public void createFolder(String folderPath, String folderName) throws NoSuchFileException {
        String owner = getOwner();
        Path path;

        if (owner.equals("anonymousUser")) {
            path = Paths.get(ROOT_PATH, folderName);
        } else {
            path = Paths.get(ROOT_PATH, owner, folderPath, folderName);
        }

        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new NoSuchFileException("test");
        }

        saveFileInfo(folderName, path.toString().replace(folderName, ""), owner, !IS_FILE);
    }


    private void saveFileInfo(String fileName, String filepath, String owner, boolean isFile) {
        userFileRepository.save(new UserFile(
                        fileName,
                        filepath,
                        owner,
                        isFile,
                        false,
                        0
                )
        );
    }

    /**
     * Load all file from a given folder.
     **/
    @Override
    public List<UserFile> loadAll(String folderPath) {
        String owner = getOwner();
        folderPath = ROOT_PATH + "\\" + owner + "\\" + folderPath + "\\";

        return userFileRepository.findByOwnerAndFilePath(owner, folderPath);
    }


    public void delete(String filepath, String fileName) {
        UserFile userFile = userFileRepository.findByFileNameAndFilePath(fileName, filepath);

        if (!userFile.getIsFile()) {
            deleteFolder(filepath, fileName);
        } else {
            deleteFile(filepath, fileName);
        }
    }


    private void deleteFolder(String filepath, String fileName) {
        String folderPath = ROOT_PATH + "\\" + getOwner() + "\\" + filepath;
        Path path = Paths.get(folderPath);

        List<UserFile> fileToDelete = userFileRepository.findByOwner(getOwner())
                .stream()
                .filter(file -> file.getFilePath().contains(path.toString()))
                .collect(Collectors.toList());


        for (UserFile file : fileToDelete) {
            userFileRepository.deleteById(file.getId());
        }

        try {
            Path finalPath = Paths.get(path.toString(), fileName);
            FileUtils.deleteDirectory(new File(finalPath.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(String filepath, String fileName) {
        String folderPath = ROOT_PATH + "\\" + getOwner();
        Path path = Paths.get(folderPath);

        UserFile file = userFileRepository.findByFileNameAndFilePath(fileName, filepath);
        userFileRepository.deleteById(file.getId());
        Path newPath = Paths.get(path.toString(), fileName);

        try {
            Files.delete(newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getOwner() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public File loadFile(String filepath, String fileName) {
        String folderPath = ROOT_PATH + "\\" + getOwner();
        Path path = Paths.get(folderPath, filepath, fileName);
        return new File(path.toString());
    }

    @Override
    public void deleteAll() {

    }
}
