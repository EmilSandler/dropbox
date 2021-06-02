package com.backend.dropbox.controller;

import com.backend.dropbox.customExceptions.InvalidDirectoryException;
import com.backend.dropbox.entity.UserFile;
import com.backend.dropbox.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.List;


/**
 * The UserFileController Class implements an Rest Controller that
 * handles File related requests.
 **/


@RestController
@RequestMapping(path = "api/v1/files")
public class UserFileController {

    private final UserFileService userFileService;

    @Autowired
    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @PostMapping("upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("filePath") String filePath) {
        try {
            userFileService.store(file, filePath);
        } catch (InvalidDirectoryException | FileAlreadyExistsException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createFolder")
    public ResponseEntity<?> createFolder(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName) {
        try {
            userFileService.createFolder(filePath, fileName);
        } catch (NoSuchFileException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName) {
        userFileService.delete(filePath, fileName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllFilesFromFolder(@RequestParam("filePath") String filePath) {
        List<UserFile> userFiles = userFileService.loadAll(filePath);
        return ResponseEntity.ok().body(userFiles);
    }

    @GetMapping("download")
    public ResponseEntity<?> downloadFile(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName) {
        File fileToDownload = userFileService.loadFile(filePath, fileName);
        InputStreamResource resource = null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        try {
            resource = new InputStreamResource(new FileInputStream(fileToDownload));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("markOrUnmarkAsFavorite")
    public ResponseEntity<?> markOrUnmarkAsFavorite(@RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName) {
        userFileService.markOrUnmarkAsFavorite(filePath, fileName);
        return ResponseEntity.ok().build();
    }
}
