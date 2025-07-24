package dev.amir.dragon.validator;

/**
 * Utility class for validating input values.
 */
public final class InputValidator {
    /**
     * Private constructor to prevent instantiation.
     */
    private InputValidator() {
    }

    public static String requireNonBlank(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value.trim();
    }

    public static void requireNonNull(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requireNull(Object value, String errorMessage) {
        if (value != null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
