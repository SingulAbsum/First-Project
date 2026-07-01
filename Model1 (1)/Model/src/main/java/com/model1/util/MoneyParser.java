package main.java.com.model1.util;

import java.math.BigDecimal;

import main.java.com.model1.exception.ValidationException;

public final class MoneyParser {
    private MoneyParser() {
    }

    public static BigDecimal parse(String value, String fieldName) {
        String normalized = ValidationUtils.requireNonBlank(value, fieldName).replace(",", ".");
        try {
            return new BigDecimal(normalized);
        } catch (NumberFormatException ex) {
            throw new ValidationException(fieldName + " must be a valid money amount.", ex);
        }
    }

    public static BigDecimal parseNonNegative(String value, String fieldName) {
        return ValidationUtils.requireNonNegative(parse(value, fieldName), fieldName);
    }
}
