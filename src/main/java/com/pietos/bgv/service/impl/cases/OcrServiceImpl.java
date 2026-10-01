package com.pietos.bgv.service.impl.cases;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.service.cases.OcrService;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${tesseract.path}")
    private String tesseractPath;

    @Value("${tesseract.language:eng}")
    private String language;

    @Override
    public String extractText(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required.");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(
                    "OCR currently supports image files only."
            );
        }

        Path originalTempFile = null;
        Path processedTempFile = null;

        try {

            // =====================================================
            // 1. SAVE ORIGINAL FILE TEMPORARILY
            // =====================================================

            String extension = getExtension(originalFileName);

            originalTempFile = Files.createTempFile(
                    "pietos-ocr-original-",
                    extension
            );

            Files.copy(
                    file.getInputStream(),
                    originalTempFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // =====================================================
            // 2. READ IMAGE
            // =====================================================

            BufferedImage originalImage =
                    ImageIO.read(originalTempFile.toFile());

            if (originalImage == null) {
                throw new IllegalArgumentException(
                        "Unable to read the uploaded image."
                );
            }

            // =====================================================
            // 3. PREPROCESS IMAGE
            // =====================================================

            BufferedImage processedImage =
                    preprocessImage(originalImage);

            // =====================================================
            // 4. WRITE PROCESSED IMAGE
            // =====================================================

            processedTempFile = Files.createTempFile(
                    "pietos-ocr-processed-",
                    ".png"
            );

            ImageIO.write(
                    processedImage,
                    "png",
                    processedTempFile.toFile()
            );

            // =====================================================
            // 5. RUN TESSERACT
            // =====================================================

            ProcessBuilder processBuilder = new ProcessBuilder(
                    tesseractPath,
                    processedTempFile.toString(),
                    "stdout",
                    "-l",
                    language,
                    "--oem",
                    "1",
                    "--psm",
                    "11",
                    "-c",
                    "preserve_interword_spaces=1"
            );

            /*
             * IMPORTANT:
             * Do NOT merge stderr with stdout.
             * Otherwise Tesseract messages can become part
             * of our OCR text.
             */
            processBuilder.redirectErrorStream(false);

            Process process = processBuilder.start();

            // =====================================================
            // 6. READ OCR OUTPUT
            // =====================================================

            String output;

            try (InputStream inputStream =
                         process.getInputStream()) {

                output = new String(
                        inputStream.readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }

            // =====================================================
            // 7. READ ERROR OUTPUT
            // =====================================================

            String errorOutput;

            try (InputStream errorStream =
                         process.getErrorStream()) {

                errorOutput = new String(
                        errorStream.readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }

            // =====================================================
            // 8. WAIT FOR TESSERACT
            // =====================================================

            boolean finished =
                    process.waitFor(60, TimeUnit.SECONDS);

            if (!finished) {

                process.destroyForcibly();

                throw new RuntimeException(
                        "OCR process timed out."
                );
            }

            int exitCode = process.exitValue();

            if (exitCode != 0) {

                throw new RuntimeException(
                        "Tesseract OCR failed. Exit code: "
                                + exitCode
                                + ". Error: "
                                + errorOutput
                );
            }

            // =====================================================
            // 9. CLEAN OCR TEXT
            // =====================================================

            return cleanOcrText(output);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to execute Tesseract OCR.",
                    e
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "OCR process was interrupted.",
                    e
            );

        } finally {

            // =====================================================
            // DELETE TEMP FILES
            // =====================================================

            deleteTempFile(originalTempFile);
            deleteTempFile(processedTempFile);
        }
    }

    // =============================================================
    // IMAGE PREPROCESSING
    // =============================================================

    private BufferedImage preprocessImage(
            BufferedImage originalImage) {

        /*
         * Upscale the image.
         *
         * Small text on Aadhaar/PAN/etc. is easier for
         * Tesseract to recognize after increasing resolution.
         */

        int scale = 2;

        int newWidth =
                originalImage.getWidth() * scale;

        int newHeight =
                originalImage.getHeight() * scale;

        BufferedImage grayImage =
                new BufferedImage(
                        newWidth,
                        newHeight,
                        BufferedImage.TYPE_BYTE_GRAY
                );

        Graphics2D graphics =
                grayImage.createGraphics();

        graphics.drawImage(
                originalImage,
                0,
                0,
                newWidth,
                newHeight,
                null
        );

        graphics.dispose();

        // =====================================================
        // THRESHOLD
        // =====================================================

        BufferedImage thresholdImage =
                new BufferedImage(
                        newWidth,
                        newHeight,
                        BufferedImage.TYPE_BYTE_BINARY
                );

        for (int y = 0; y < newHeight; y++) {

            for (int x = 0; x < newWidth; x++) {

                int rgb =
                        grayImage.getRGB(x, y);

                int gray =
                        rgb & 0xFF;

                /*
                 * Convert to black/white.
                 *
                 * Pixels darker than 180 become black.
                 * Pixels lighter than 180 become white.
                 */

                if (gray < 180) {

                    thresholdImage.setRGB(
                            x,
                            y,
                            0xFF000000
                    );

                } else {

                    thresholdImage.setRGB(
                            x,
                            y,
                            0xFFFFFFFF
                    );
                }
            }
        }

        return thresholdImage;
    }

    // =============================================================
    // OCR TEXT CLEANING
    // =============================================================

    private String cleanOcrText(String output) {

        if (output == null || output.isBlank()) {
            return "";
        }

        String[] lines = output.split("\\R");

        StringBuilder cleaned =
                new StringBuilder();

        for (String line : lines) {

            if (line == null) {
                continue;
            }

            String cleanLine =
                    line.trim();

            if (cleanLine.isEmpty()) {
                continue;
            }

            // Remove excessive spaces
            cleanLine =
                    cleanLine.replaceAll("\\s{2,}", " ");

            cleaned.append(cleanLine);
            cleaned.append(System.lineSeparator());
        }

        return cleaned.toString().trim();
    }

    // =============================================================
    // FILE EXTENSION
    // =============================================================

    private String getExtension(String fileName) {

        int lastDot =
                fileName.lastIndexOf('.');

        if (lastDot == -1) {
            return ".tmp";
        }

        return fileName.substring(lastDot);
    }

    // =============================================================
    // DELETE TEMP FILE
    // =============================================================

    private void deleteTempFile(Path file) {

        if (file == null) {
            return;
        }

        try {

            Files.deleteIfExists(file);

        } catch (IOException e) {

            System.err.println(
                    "Unable to delete temporary OCR file: "
                            + file
            );
        }
    }
}