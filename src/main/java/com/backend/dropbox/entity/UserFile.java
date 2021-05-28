package com.backend.dropbox.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UserFile {

    @Id
    private String id;

    private String fileName;

    private String filePath;

    private String owner;

    private Boolean isFile;

    private Boolean favorite;

    private int sharedCount;

    public UserFile(String fileName, String filePath, String owner, Boolean isFile, Boolean favorite, int sharedCount) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.owner = owner;
        this.isFile = isFile;
        this.favorite = favorite;
        this.sharedCount = sharedCount;
    }
}
