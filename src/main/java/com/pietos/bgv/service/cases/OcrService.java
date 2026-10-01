package com.pietos.bgv.service.cases;

import org.springframework.web.multipart.MultipartFile;

public interface OcrService {

    String extractText(MultipartFile file);
}