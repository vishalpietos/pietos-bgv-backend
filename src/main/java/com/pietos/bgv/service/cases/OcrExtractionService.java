package com.pietos.bgv.service.cases;

import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.response.cases.OcrExtractionResponse;

public interface OcrExtractionService {

    OcrExtractionResponse extractDocumentData(MultipartFile file);
}