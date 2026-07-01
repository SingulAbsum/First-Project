package main.java.com.model1.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import main.java.com.model1.exception.ValidationException;

public final class DateParser {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter LEGACY_DATE = DateTimeFormatter.ofPattern("MMddyyyy");

    private DateParser() {
    }

    public static LocalDate parseIso(String value, String fieldName) {
        String normalized = ValidationUtils.requireNonBlank(value, fieldName);
        try {
            return LocalDate.parse(normalized, ISO_DATE);
        } catch (DateTimeParseException ex) {
            throw new ValidationException(fieldName + " must use yyyy-MM-dd format.", ex);
        }
    }

    public static LocalDate parseLegacy(String value, String fieldName) {
        String normalized = ValidationUtils.requireNonBlank(value, fieldName).replaceAll("[^0-9]", "");
        if (normalized.length() == 7) {
            normalized = "0" + normalized;
        }
        try {
            return LocalDate.parse(normalized, LEGACY_DATE);
        } catch (DateTimeParseException ex) {
            throw new ValidationException(fieldName + " must use MMddyyyy format.", ex);
        }
    }

    public static LocalDate parseIsoOrLegacy(String value, String fieldName) {
        try {
            return parseIso(value, fieldName);
        } catch (ValidationException ignored) {
            return parseLegacy(value, fieldName);
        }
    }
}
