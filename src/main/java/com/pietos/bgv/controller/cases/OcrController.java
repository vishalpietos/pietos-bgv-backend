package com.pietos.bgv.controller.cases;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.response.ApiResponse;
import com.pietos.bgv.dto.response.cases.OcrExtractionResponse;
import com.pietos.bgv.service.cases.OcrExtractionService;

@RestController
@RequestMapping("/api/cases/ocr")
public class OcrController {

    private final OcrExtractionService ocrExtractionService;

    public OcrController(
            OcrExtractionService ocrExtractionService) {

        this.ocrExtractionService =
                ocrExtractionService;
    }

    // =====================================================
    // EXTRACT DOCUMENT DATA
    // =====================================================

    @PostMapping("/extract")
    public ResponseEntity<ApiResponse<OcrExtractionResponse>>
            extractText(
                    @RequestParam("file") MultipartFile file) {

        OcrExtractionResponse response =
                ocrExtractionService.extractDocumentData(file);

        ApiResponse<OcrExtractionResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "OCR data extracted successfully.",
                        response
                );

        return ResponseEntity.ok(apiResponse);
    }
}