package com.pietos.bgv.service.impl.cases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.pietos.bgv.dto.imports.cases.CaseImportError;
import com.pietos.bgv.dto.imports.cases.CaseImportRow;
import com.pietos.bgv.repository.cases.CaseRepository;
import com.pietos.bgv.service.cases.CaseImportValidationService;

@Service
public class CaseImportValidationServiceImpl
        implements CaseImportValidationService {

    private final CaseRepository caseRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yy");


    public CaseImportValidationServiceImpl(
            CaseRepository caseRepository) {

        this.caseRepository = caseRepository;
    }


    @Override
    public List<CaseImportError> validate(
            List<CaseImportRow> rows) {

        List<CaseImportError> errors = new ArrayList<>();

        Set<String> employeeIds =new HashSet<>();

        Set<String> mobileNumbers =new HashSet<>();

        Set<String> emails = new HashSet<>();


        for (CaseImportRow row : rows) {

            // =============================================
            // CLIENT CODE
            // =============================================

            if (isBlank(row.getClientCode())) {

                addError(
                        errors,
                        row,
                        "Client Code",
                        "Client Code is required."
                );
            }


            // =============================================
            // CLIENT LOCATION
            // =============================================

            if (isBlank(row.getClientLocation())) {

                addError(
                        errors,
                        row,
                        "Client Location",
                        "Client Location is required."
                );
            }


            // =============================================
            // CASE RECEIVED DATE
            // =============================================

            if (isBlank(row.getCaseReceivedDate())) {

                addError(
                        errors,
                        row,
                        "Case Received Date",
                        "Case Received Date is required."
                );

            } else {

                validateDate(
                        errors,
                        row,
                        "Case Received Date",
                        row.getCaseReceivedDate()
                );
            }


            // =============================================
            // EMPLOYMENT
            // =============================================

            if (isBlank(row.getEmployment())) {

                addError(
                        errors,
                        row,
                        "Employment",
                        "Employment is required."
                );

            } else {

                String employment =
                        row.getEmployment().trim();

                if (!employment.equalsIgnoreCase("PRE_EMPLOYMENT")
                        && !employment.equalsIgnoreCase("POST_EMPLOYMENT")) {

                    addError(
                            errors,
                            row,
                            "Employment",
                            "Employment must be PRE_EMPLOYMENT or POST_EMPLOYMENT."
                    );
                }
            }


            // =============================================
            // FIRST NAME
            // =============================================

            if (isBlank(row.getFirstName())) {

                addError(
                        errors,
                        row,
                        "First Name",
                        "First Name is required."
                );
            }


            // =============================================
            // DATE OF BIRTH
            // =============================================

            if (!isBlank(row.getDateOfBirth())) {

                validateDate(
                        errors,
                        row,
                        "Date of Birth",
                        row.getDateOfBirth()
                );
            }


            // =============================================
            // CLIENT EMPLOYEE ID
            // =============================================

            if (isBlank(row.getClientEmployeeId())) {

                addError(
                        errors,
                        row,
                        "Client Employee ID",
                        "Client Employee ID is required."
                );

            } else {

                String employeeId =
                        row.getClientEmployeeId()
                                .trim()
                                .toLowerCase();

                if (!employeeIds.add(employeeId)) {

                    addError(
                            errors,
                            row,
                            "Client Employee ID",
                            "Duplicate Client Employee ID in Excel."
                    );

                } else if (
                        caseRepository.existsByClientEmployeeId(
                                row.getClientEmployeeId().trim())) {

                    addError(
                            errors,
                            row,
                            "Client Employee ID",
                            "Client Employee ID already exists."
                    );
                }
            }


            // =============================================
            // DATE OF JOINING
            // =============================================

            if (!isBlank(row.getDateOfJoining())) {

                validateDate(
                        errors,
                        row,
                        "Date of Joining",
                        row.getDateOfJoining()
                );
            }


            // =============================================
            // MOBILE NUMBER
            // =============================================

            if (isBlank(row.getMobileNumber())) {

                addError(
                        errors,
                        row,
                        "Mobile Number",
                        "Mobile Number is required."
                );

            } else {

                String mobile =
                        row.getMobileNumber().trim();

                if (!mobile.matches("\\d{10}")) {

                    addError(
                            errors,
                            row,
                            "Mobile Number",
                            "Mobile Number must contain exactly 10 digits."
                    );

                } else if (!mobileNumbers.add(mobile)) {

                    addError(
                            errors,
                            row,
                            "Mobile Number",
                            "Duplicate Mobile Number in Excel."
                    );

                } else if (
                        caseRepository.existsByMobileNumber(mobile)) {

                    addError(
                            errors,
                            row,
                            "Mobile Number",
                            "Mobile Number already exists."
                    );
                }
            }


            // =============================================
            // EMAIL
            // =============================================

            if (isBlank(row.getEmail())) {

                addError(
                        errors,
                        row,
                        "Email",
                        "Email is required."
                );

            } else {

                String email =
                        row.getEmail()
                                .trim()
                                .toLowerCase();

                if (!isValidEmail(email)) {

                    addError(
                            errors,
                            row,
                            "Email",
                            "Invalid email format."
                    );

                } else if (!emails.add(email)) {

                    addError(
                            errors,
                            row,
                            "Email",
                            "Duplicate Email in Excel."
                    );

                } else if (
                        caseRepository.existsByEmailIgnoreCase(email)) {

                    addError(
                            errors,
                            row,
                            "Email",
                            "Email already exists."
                    );
                }
            }


            // =============================================
            // GENDER
            // =============================================

            if (isBlank(row.getGender())) {

                addError(
                        errors,
                        row,
                        "Gender",
                        "Gender is required."
                );

            } else {

                String gender =
                        row.getGender().trim();

                if (!gender.equalsIgnoreCase("Male")
                        && !gender.equalsIgnoreCase("Female")) {

                    addError(
                            errors,
                            row,
                            "Gender",
                            "Gender must be Male or Female."
                    );
                }
            }
        }

        return errors;
    }


    private void validateDate(
            List<CaseImportError> errors,
            CaseImportRow row,
            String field,
            String value) {

        try {

            LocalDate.parse(
                    value.trim(),
                    DATE_FORMAT
            );

        } catch (DateTimeParseException ex) {

            addError(
                    errors,
                    row,
                    field,
                    field + " must be in DD-MM-YY format."
            );
        }
    }


    private boolean isValidEmail(String email) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }


    private boolean isBlank(String value) {

        return value == null
                || value.trim().isEmpty();
    }


    private void addError(
            List<CaseImportError> errors,
            CaseImportRow row,
            String field,
            String message) {

        errors.add(
                new CaseImportError(
                        row.getRowNumber(),
                        field,
                        message
                )
        );
    }
}