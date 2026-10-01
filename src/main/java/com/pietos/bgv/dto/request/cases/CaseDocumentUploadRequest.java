package com.pietos.bgv.dto.request.cases;

import org.springframework.web.multipart.MultipartFile;

public class CaseDocumentUploadRequest {

    private String description;

    private MultipartFile file;


    public CaseDocumentUploadRequest() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}