package com.pietos.bgv.service.impl;

	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.nio.file.StandardCopyOption;
	import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
	import org.springframework.web.multipart.MultipartFile;

	import com.pietos.bgv.service.FileStorageService;

	@Service
	public class FileStorageServiceImpl implements FileStorageService {

	    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

	    @Value("${file.upload-dir}")
	    private String uploadDirectory;

	    @Override
	    public String uploadFile(MultipartFile file, String folderName) {

	        if (file == null || file.isEmpty()) {
	            throw new IllegalArgumentException("Please select a file.");
	        }

	        if (file.getSize() > MAX_FILE_SIZE) {
	            throw new IllegalArgumentException("File size should not exceed 10 MB.");
	        }

	        try {

	            Path uploadPath = Paths.get(uploadDirectory, folderName);

	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }

	            String originalFileName = file.getOriginalFilename();

	            String extension = "";

	            if (originalFileName != null && originalFileName.contains(".")) {
	                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
	            }

	            String fileName = UUID.randomUUID() + extension;

	            Path filePath = uploadPath.resolve(fileName);

	            Files.copy(
	                    file.getInputStream(),
	                    filePath,
	                    StandardCopyOption.REPLACE_EXISTING);

	            return filePath.toString().replace("\\", "/");

	        } catch (IOException exception) {

	            throw new RuntimeException(
	                    "Unable to upload file.",
	                    exception);
	        }
	    }
	}
