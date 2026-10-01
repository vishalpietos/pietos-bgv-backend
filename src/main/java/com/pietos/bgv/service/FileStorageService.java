package com.pietos.bgv.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadFile(
            MultipartFile file,
            String folderName);
}