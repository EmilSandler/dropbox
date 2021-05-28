package com.backend.dropbox.repository;

import com.backend.dropbox.entity.UserFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserFileRepository extends MongoRepository<UserFile, String> {
    List<UserFile> findByOwnerAndFilePath(String owner, String filePath);

    List<UserFile> findByOwner(String owner);

    UserFile findByFileNameAndFilePath(String fileName, String filePath);


}
