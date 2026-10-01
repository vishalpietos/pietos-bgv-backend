package com.pietos.bgv.util;

public final class CaseReferenceUtil {

    private CaseReferenceUtil() {
    }

    public static String generateCaseReference(
            String clientAbbreviation,
            Long clientId,
            Long caseId) {

        return "PT/"
                + clientAbbreviation
                + "/"
                + clientId
                + "/"
                + caseId;
    }
}