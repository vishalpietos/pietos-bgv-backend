package com.pietos.bgv.service.impl.cases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.response.cases.OcrExtractionResponse;
import com.pietos.bgv.service.cases.OcrService;
import com.pietos.bgv.service.cases.OcrExtractionService;

@Service
public class OcrExtractionServiceImpl
        implements OcrExtractionService {

    private final OcrService ocrService;

    public OcrExtractionServiceImpl(
            OcrService ocrService) {

        this.ocrService = ocrService;
    }

    @Override
    public OcrExtractionResponse extractDocumentData(
            MultipartFile file) {

        // =====================================================
        // 1. RUN OCR
        // =====================================================

        String rawText =
                ocrService.extractText(file);

        if (rawText == null || rawText.isBlank()) {
            throw new IllegalArgumentException(
                    "No text could be extracted from the document."
            );
        }

        // =====================================================
        // 2. DETECT DOCUMENT TYPE
        // =====================================================

        String documentType =
                detectDocumentType(rawText);

        // =====================================================
        // 3. RESPONSE
        // =====================================================

        OcrExtractionResponse response =
                new OcrExtractionResponse();

        response.setSuccess(true);
        response.setDocumentType(documentType);
        response.setRawText(rawText);

        // =====================================================
        // 4. DOCUMENT-SPECIFIC PARSER
        // =====================================================

        switch (documentType) {

            case "AADHAAR":
                parseAadhaar(rawText, response);
                break;

            case "PAN":
                parsePan(rawText, response);
                break;

            case "EDUCATION_DOCUMENT":
                parseEducationDocument(rawText, response);
                break;

            default:
                break;
        }

        return response;
    }

    // =========================================================
    // DOCUMENT TYPE DETECTION
    // =========================================================

    private String detectDocumentType(String text) {

        if (text == null || text.isBlank()) {
            return "UNKNOWN";
        }

        String normalized =
                text.toLowerCase();

        // =====================================================
        // PAN
        // =====================================================

        Pattern panPattern =
                Pattern.compile(
                        "\\b[A-Z]{5}[0-9]{4}[A-Z]\\b",
                        Pattern.CASE_INSENSITIVE
                );

        if (panPattern.matcher(text).find()
                || normalized.contains(
                        "permanent account number")
                || normalized.contains(
                        "income tax department")) {

            return "PAN";
        }

        // =====================================================
        // AADHAAR
        // =====================================================

        Pattern aadhaarPattern =
                Pattern.compile(
                        "(?<!\\d)\\d{4}\\s*\\d{4}\\s*\\d{4}(?!\\d)"
                );

        boolean hasAadhaarNumber =
                aadhaarPattern.matcher(text).find();

        boolean hasAadhaarKeyword =
                normalized.contains("aadhaar")
                        || normalized.contains("aadhar")
                        || normalized.contains("uidai")
                        || normalized.contains(
                                "unique identification");

        if (hasAadhaarNumber
                || hasAadhaarKeyword) {

            return "AADHAAR";
        }

        // =====================================================
        // EDUCATION DOCUMENT
        // =====================================================

        int educationSignals = 0;

        if (containsAny(
                normalized,
                "university",
                "college",
                "institute",
                "institution")) {

            educationSignals++;
        }

        if (containsAny(
                normalized,
                "degree",
                "diploma",
                "certificate",
                "marksheet",
                "mark sheet",
                "bachelor",
                "master",
                "graduation",
                "post graduation",
                "qualification")) {

            educationSignals++;
        }

        if (containsAny(
                normalized,
                "registration no",
                "registration number",
                "enrollment no",
                "enrollment number",
                "roll no",
                "certificate no",
                "certificate number")) {

            educationSignals++;
        }

        if (containsAny(
                normalized,
                "year of passing",
                "year of completion",
                "date of passing",
                "passed in")) {

            educationSignals++;
        }

        /*
         * Require more than one education signal.
         * This prevents an ordinary document containing
         * one word such as "certificate" from being classified
         * automatically as an education document.
         */

        if (educationSignals >= 2) {
            return "EDUCATION_DOCUMENT";
        }

        return "UNKNOWN";
    }

    // =========================================================
    // EDUCATION DOCUMENT PARSER
    // =========================================================

    private void parseEducationDocument(
            String text,
            OcrExtractionResponse response) {

        String normalizedText =
                normalizeText(text);

        // =====================================================
        // UNIVERSITY
        // =====================================================

        String university =
                extractLabeledValue(
                        normalizedText,
                        "university"
                );

        response.setUniversity(university);

        // =====================================================
        // INSTITUTION
        // =====================================================

        String institution =
                extractLabeledValue(
                        normalizedText,
                        "institution"
                );

        if (institution == null) {

            institution =
                    extractLabeledValue(
                            normalizedText,
                            "college"
                    );
        }

        if (institution == null) {

            institution =
                    extractLabeledValue(
                            normalizedText,
                            "institute"
                    );
        }

        response.setInstitution(institution);

        // =====================================================
        // REGISTRATION / CERTIFICATE NUMBER
        // =====================================================

        String registrationNumber =
                extractRegistrationNumber(normalizedText);

        response.setRegistrationNumber(
                registrationNumber
        );

        // =====================================================
        // QUALIFICATION
        // =====================================================

        String qualification =
                extractQualification(normalizedText);

        response.setQualification(
                qualification
        );

        // =====================================================
        // YEAR OF PASSING
        // =====================================================

        String yearOfPassing =
                extractYearOfPassing(normalizedText);

        response.setYearOfPassing(
                yearOfPassing
        );
    }

    // =========================================================
    // EXTRACT LABELED VALUE
    // =========================================================

    private String extractLabeledValue(
            String text,
            String label) {

        Pattern pattern =
                Pattern.compile(
                        "(?im)^\\s*"
                                + Pattern.quote(label)
                                + "\\s*[:/\\-]?\\s*"
                                + "(.+?)\\s*$"
                );

        Matcher matcher =
                pattern.matcher(text);

        if (matcher.find()) {

            String value =
                    cleanValue(matcher.group(1));

            return isUsefulValue(value)
                    ? value
                    : null;
        }

        return null;
    }

    // =========================================================
    // REGISTRATION / CERTIFICATE NUMBER
    // =========================================================

    private String extractRegistrationNumber(
            String text) {

        Pattern pattern =
                Pattern.compile(
                        "(?im)"
                                + "(?:registration\\s*(?:no|number)?"
                                + "|enrollment\\s*(?:no|number)?"
                                + "|certificate\\s*(?:no|number)?"
                                + "|roll\\s*(?:no|number)?)"
                                + "\\s*[:/\\-]?\\s*"
                                + "([A-Z0-9][A-Z0-9\\-/ ]{2,})"
                );

        Matcher matcher =
                pattern.matcher(text);

        if (matcher.find()) {

            return cleanValue(
                    matcher.group(1)
            );
        }

        return null;
    }

    // =========================================================
    // QUALIFICATION
    // =========================================================

    private String extractQualification(
            String text) {

        Pattern pattern =
                Pattern.compile(
                        "(?im)"
                                + "(?:qualification|degree|course)"
                                + "\\s*[:/\\-]?\\s*"
                                + "(.+?)\\s*$"
                );

        Matcher matcher =
                pattern.matcher(text);

        if (matcher.find()) {

            return cleanValue(
                    matcher.group(1)
            );
        }

        // =====================================================
        // COMMON QUALIFICATION PATTERNS
        // =====================================================

        Pattern commonQualificationPattern =
                Pattern.compile(
                        "(?i)\\b("
                                + "b\\.?\\s*tech"
                                + "|b\\.?\\s*e"
                                + "|b\\.?\\s*sc"
                                + "|b\\.?\\s*com"
                                + "|b\\.?\\s*a"
                                + "|m\\.?\\s*tech"
                                + "|m\\.?\\s*e"
                                + "|m\\.?\\s*sc"
                                + "|m\\.?\\s*com"
                                + "|m\\.?\\s*a"
                                + "|mba"
                                + "|mca"
                                + "|bca"
                                + "|phd"
                                + "|diploma"
                                + "|bachelor[^\\n]*"
                                + "|master[^\\n]*"
                                + "|post graduate[^\\n]*"
                                + "|postgraduate[^\\n]*"
                                + ")\\b"
                );

        Matcher matcher1 =
                commonQualificationPattern.matcher(text);

        if (matcher1.find()) {

            return cleanValue(
                    matcher.group(1)
            );
        }

        return null;
    }

    // =========================================================
    // YEAR OF PASSING
    // =========================================================

    private String extractYearOfPassing(
            String text) {

        Pattern labeledPattern =
                Pattern.compile(
                        "(?im)"
                                + "(?:year\\s+of\\s+passing"
                                + "|year\\s+of\\s+completion"
                                + "|date\\s+of\\s+passing"
                                + "|passed\\s+in)"
                                + "\\s*[:/\\-]?\\s*"
                                + "(19\\d{2}|20\\d{2})"
                );

        Matcher matcher =
                labeledPattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        /*
         * Fallback:
         * Search for a standalone 4-digit year.
         *
         * This is intentionally done only after the labeled
         * search because education certificates often contain
         * multiple dates.
         */

        Pattern yearPattern =
                Pattern.compile(
                        "(?<!\\d)(19\\d{2}|20\\d{2})(?!\\d)"
                );

        matcher =
                yearPattern.matcher(text);

        while (matcher.find()) {

            String year =
                    matcher.group(1);

            int yearValue =
                    Integer.parseInt(year);

            int currentYear =
                    java.time.Year.now().getValue();

            /*
             * Ignore unlikely future years.
             */

            if (yearValue <= currentYear
                    && yearValue >= 1950) {

                return year;
            }
        }

        return null;
    }

    // =========================================================
    // AADHAAR
    // =========================================================

    private void parseAadhaar(
            String text,
            OcrExtractionResponse response) {

        response.setDateOfBirth(
                extractDateOfBirth(text)
        );

        response.setGender(
                extractGender(text)
        );

        response.setDocumentNumber(
                extractAadhaarNumber(text)
        );
    }

    // =========================================================
    // PAN
    // =========================================================

    private void parsePan(
            String text,
            OcrExtractionResponse response) {

        Pattern panPattern =
                Pattern.compile(
                        "\\b[A-Z]{5}[0-9]{4}[A-Z]\\b"
                );

        Matcher panMatcher =
                panPattern.matcher(
                        text.toUpperCase()
                );

        if (panMatcher.find()) {

            response.setDocumentNumber(
                    panMatcher.group()
            );
        }

        response.setName(
                extractLabeledValue(
                        normalizeText(text),
                        "name"
                )
        );

        response.setDateOfBirth(
                extractDateOfBirth(text)
        );
    }

    // =========================================================
    // DATE OF BIRTH
    // =========================================================

    private String extractDateOfBirth(
            String text) {

        Pattern pattern =
                Pattern.compile(
                        "(?i)"
                                + "(?:dob|date\\s+of\\s+birth)"
                                + "\\s*[:/\\-]?\\s*"
                                + "(\\d{1,2}[/-]\\d{1,2}[/-]\\d{4})"
                );

        Matcher matcher =
                pattern.matcher(text);

        if (matcher.find()) {

            return matcher.group(1);
        }

        return null;
    }

    // =========================================================
    // GENDER
    // =========================================================

    private String extractGender(
            String text) {

        if (Pattern
                .compile("(?i)\\b(female|mahila)\\b")
                .matcher(text)
                .find()) {

            return "Female";
        }

        if (Pattern
                .compile("(?i)\\b(male|purush)\\b")
                .matcher(text)
                .find()) {

            return "Male";
        }

        return null;
    }

    // =========================================================
    // AADHAAR NUMBER
    // =========================================================

    private String extractAadhaarNumber(
            String text) {

        Pattern pattern =
                Pattern.compile(
                        "(?<!\\d)"
                                + "(\\d{4}\\s*\\d{4}\\s*\\d{4})"
                                + "(?!\\d)"
                );

        Matcher matcher =
                pattern.matcher(text);

        if (matcher.find()) {

            return matcher.group(1)
                    .replaceAll("\\s+", "");
        }

        return null;
    }

    // =========================================================
    // NORMALIZE TEXT
    // =========================================================

    private String normalizeText(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\r\n", "\n")
                .replace('\r', '\n');
    }

    // =========================================================
    // CLEAN VALUE
    // =========================================================

    private String cleanValue(
            String value) {

        if (value == null) {
            return null;
        }

        return value
                .replaceAll("\\s{2,}", " ")
                .replaceAll("\\s*\\|\\s*", " ")
                .trim();
    }

    // =========================================================
    // CHECK USEFUL VALUE
    // =========================================================

    private boolean isUsefulValue(
            String value) {

        if (value == null || value.isBlank()) {
            return false;
        }

        String normalized =
                value.toLowerCase();

        return !normalized.equals("name")
                && !normalized.equals("university")
                && !normalized.equals("institution")
                && !normalized.equals("college")
                && !normalized.equals("institute");
    }

    // =========================================================
    // CONTAINS ANY
    // =========================================================

    private boolean containsAny(
            String text,
            String... values) {

        for (String value : values) {

            if (text.contains(value)) {
                return true;
            }
        }

        return false;
    }
}