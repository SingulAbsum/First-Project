package main.java.com.model1.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import main.java.com.model1.exception.ValidationException;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " is required.");
        }
        return value;
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
        return value.trim();
    }

    public static int requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be positive.");
        }
        return value;
    }

    public static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.signum() < 0) {
            throw new ValidationException(fieldName + " cannot be negative.");
        }
        return value;
    }

    public static <T extends Collection<?>> T requireNotEmpty(T value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new ValidationException(fieldName + " must not be empty.");
        }
        return value;
    }

    public static void requireDateRange(LocalDate startDate, LocalDate endDate) {
        requireNonNull(startDate, "Start date");
        requireNonNull(endDate, "End date");
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before or equal to end date.");
        }
    }
}
