package main.java.com.model1.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

final class RepositoryMappers {
    private static final DateTimeFormatter LEGACY_DATE = DateTimeFormatter.ofPattern("MMddyyyy");

    private RepositoryMappers() {
    }

    static BigDecimal decimal(ResultSet resultSet, String column) throws SQLException {
        String value = resultSet.getString(column);
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }

    static LocalDate legacyDate(ResultSet resultSet, String column) throws SQLException {
        String value = resultSet.getString(column);
        return parseLegacyDate(value);
    }

    static LocalDate parseLegacyDate(String value) {
        if (value == null || value.isBlank()) {
            return LocalDate.now();
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() == 7) {
            digits = "0" + digits;
        }
        if (digits.length() == 8) {
            try {
                return LocalDate.parse(digits, LEGACY_DATE);
            } catch (RuntimeException ignored) {
                return LocalDate.now();
            }
        }
        return LocalDate.now();
    }

    static String formatLegacyDate(LocalDate date) {
        return LEGACY_DATE.format(date);
    }
}
